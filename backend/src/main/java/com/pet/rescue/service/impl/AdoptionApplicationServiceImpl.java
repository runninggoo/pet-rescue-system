package com.pet.rescue.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pet.rescue.entity.AdoptionApplication;
import com.pet.rescue.entity.Pet;
import com.pet.rescue.entity.User;
import com.pet.rescue.mapper.AdoptionApplicationMapper;
import com.pet.rescue.mapper.PetMapper;
import com.pet.rescue.mapper.UserMapper;
import com.pet.rescue.service.AdoptionApplicationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class AdoptionApplicationServiceImpl extends ServiceImpl<AdoptionApplicationMapper, AdoptionApplication>
        implements AdoptionApplicationService {

    /** 申请状态常量 */
    public static final int STATUS_PENDING = 0;       // 待审核
    public static final int STATUS_APPROVED = 1;       // 已通过
    public static final int STATUS_REJECTED = 2;       // 已拒绝
    public static final int STATUS_NEED_MORE = 3;       // 待补充
    public static final int STATUS_PENDING_SIGN = 4;    // 待签署
    public static final int STATUS_COMPLETED = 5;       // 已完成

    private final AdoptionApplicationMapper applicationMapper;
    private final PetMapper petMapper;
    private final UserMapper userMapper;

    public AdoptionApplicationServiceImpl(AdoptionApplicationMapper applicationMapper,
                                          PetMapper petMapper,
                                          UserMapper userMapper) {
        this.applicationMapper = applicationMapper;
        this.petMapper = petMapper;
        this.userMapper = userMapper;
    }

    @Override
    public boolean submitApplication(AdoptionApplication application) {
        // 1. 检查宠物是否可领养（status=0 表示待领养）
        Pet pet = petMapper.selectById(application.getPetId());
        if (pet == null) {
            throw new RuntimeException("宠物不存在");
        }
        if (pet.getStatus() == null || pet.getStatus() != 0) {
            throw new RuntimeException("该宠物当前不可申请领养，可能已被领养或已下架");
        }

        // 2. 检查是否已申请（仅检查待审核和待补充状态，防止重复申请）
        if (hasApplied(application.getPetId(), application.getApplicantId())) {
            throw new RuntimeException("您已对该宠物提交过申请，请等待审核结果");
        }

        // 3. 自动填充机构ID（从宠物信息中获取）
        if (application.getInstitutionId() == null) {
            application.setInstitutionId(pet.getInstitutionId());
        }

        // 4. 设置默认状态和时间
        application.setStatus(STATUS_PENDING);
        application.setApplyDate(new Date());
        return applicationMapper.insert(application) > 0;
    }

    @Override
    public List<AdoptionApplication> findByApplicantId(Long applicantId) {
        List<AdoptionApplication> list = applicationMapper.selectByApplicantId(applicantId);
        return enrichWithDetails(list);
    }

    @Override
    public List<AdoptionApplication> findByPetId(Long petId) {
        List<AdoptionApplication> list = applicationMapper.selectByPetId(petId);
        return enrichWithDetails(list);
    }

    @Override
    public List<AdoptionApplication> findByStatus(Integer status) {
        // 使用原生 @Select 避免拦截器链导致的 jsqlparser 版本冲突问题
        List<AdoptionApplication> list = applicationMapper.selectList(
                lambdaQuery().eq(status != null && status >= 0, AdoptionApplication::getStatus, status)
                             .orderByDesc(AdoptionApplication::getApplyDate));
        return enrichWithDetails(list);
    }

    @Override
    public boolean reviewApplication(Long applicationId, Integer status, String reviewComment) {
        AdoptionApplication application = applicationMapper.selectById(applicationId);
        if (application == null) {
            throw new RuntimeException("申请记录不存在");
        }

        // 状态机验证：只有待审核(0)的申请才能被审核
        if (!canTransition(application.getStatus(), status)) {
            throw new RuntimeException("该申请当前状态不允许执行此操作");
        }

        // 验证审核结果
        if (status == STATUS_REJECTED && (reviewComment == null || reviewComment.trim().isEmpty())) {
            throw new RuntimeException("拒绝申请时必须填写审核意见");
        }

        // 更新申请状态
        application.setStatus(status);
        application.setReviewComment(reviewComment);
        application.setReviewTime(new Date());

        // 如果审核通过(1)，自动进入待签署(4)状态
        if (status == STATUS_APPROVED) {
            application.setStatus(STATUS_PENDING_SIGN);
            application.setReviewComment((reviewComment != null ? reviewComment : "审核通过") + " — 请签署领养协议");
        }

        boolean updated = applicationMapper.updateById(application) > 0;

        // 审核通过后：
        // 1. 将该宠物状态改为"已领养"(1)
        // 2. 将该宠物下的其他待审核申请全部标记为已拒绝（因已被他人抢先）
        if (status == STATUS_APPROVED) {
            // 更新宠物状态为已领养
            Pet pet = petMapper.selectById(application.getPetId());
            if (pet != null) {
                LambdaUpdateWrapper<Pet> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.eq(Pet::getId, application.getPetId()).set(Pet::getStatus, 1);
                petMapper.update(null, updateWrapper);
            }
            rejectOtherPendingApplications(application.getPetId(), applicationId);
        }

        return updated;
    }

    @Override
    public boolean hasApplied(Long petId, Long applicantId) {
        return applicationMapper.findByPetIdAndApplicantId(petId, applicantId) != null;
    }

    @Override
    public boolean updateStatus(Long applicationId, Integer status) {
        AdoptionApplication application = applicationMapper.selectById(applicationId);
        if (application == null) {
            throw new RuntimeException("申请记录不存在");
        }
        if (!canTransition(application.getStatus(), status)) {
            throw new RuntimeException("当前状态不允许转换到目标状态");
        }
        application.setStatus(status);
        return applicationMapper.updateById(application) > 0;
    }

    @Override
    public IPage<AdoptionApplication> findByCondition(Map<String, Object> params) {
        int page = 1;
        int pageSize = 10;
        if (params != null) {
            if (params.containsKey("page")) {
                try { page = Integer.parseInt(params.get("page").toString()); } catch (Exception ignored) {}
            }
            if (params.containsKey("pageSize")) {
                try { pageSize = Integer.parseInt(params.get("pageSize").toString()); } catch (Exception ignored) {}
            }
        }

        Integer status = params.containsKey("status") ? (Integer) params.get("status") : null;
        Long institutionId = params.containsKey("institutionId") ? (Long) params.get("institutionId") : null;

        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<AdoptionApplication> queryWrapper =
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();

        if (status != null && status >= 0) {
            queryWrapper.eq(AdoptionApplication::getStatus, status);
        }

        if (institutionId != null) {
            queryWrapper.eq(AdoptionApplication::getInstitutionId, institutionId);
        }

        queryWrapper.orderByDesc(AdoptionApplication::getApplyDate);

        // 手动分页：先查出全部符合条件的数据，再截取当前页
        List<AdoptionApplication> allRecords = applicationMapper.selectList(queryWrapper);
        long total = allRecords.size();
        int fromIndex = (int) Math.min((long) (page - 1) * pageSize, total);
        int toIndex = (int) Math.min(fromIndex + pageSize, total);
        List<AdoptionApplication> pagedRecords = (fromIndex < total) ? allRecords.subList(fromIndex, toIndex) : List.of();

        // 填充关联信息
        if (!pagedRecords.isEmpty()) {
            enrichWithDetails(pagedRecords);
        }

        // 构建分页结果（total accurate，isSearchCount=false 避免二次 COUNT）
        Page<AdoptionApplication> pageResult = new Page<>(page, pageSize, total, false);
        pageResult.setRecords(pagedRecords);
        return pageResult;
    }

    @Override
    public AdoptionApplication findById(Long applicationId) {
        AdoptionApplication application = applicationMapper.selectById(applicationId);
        if (application != null) {
            return enrichWithDetails(List.of(application)).get(0);
        }
        return null;
    }

    @Override
    public boolean signContract(Long applicationId) {
        AdoptionApplication application = applicationMapper.selectById(applicationId);
        if (application == null) {
            throw new RuntimeException("申请记录不存在");
        }

        // 只有待签署(4)状态的申请才能签署
        if (!canTransition(application.getStatus(), STATUS_COMPLETED)) {
            throw new RuntimeException("该申请当前状态不允许签署领养协议");
        }

        application.setStatus(STATUS_COMPLETED);
        return applicationMapper.updateById(application) > 0;
    }

    @Override
    public Map<Integer, Long> getStatusCounts() {
        Map<Integer, Long> counts = new HashMap<>();
        // 初始化所有状态计数为0
        counts.put(STATUS_PENDING, 0L);
        counts.put(STATUS_APPROVED, 0L);
        counts.put(STATUS_REJECTED, 0L);
        counts.put(STATUS_NEED_MORE, 0L);
        counts.put(STATUS_PENDING_SIGN, 0L);
        counts.put(STATUS_COMPLETED, 0L);

        List<Map<String, Object>> result = applicationMapper.countByStatus();
        if (result != null) {
            for (Map<String, Object> row : result) {
                Object statusObj = row.get("status");
                Object countObj = row.get("count");
                int status = statusObj != null ? ((Number) statusObj).intValue() : -1;
                long count = countObj != null ? ((Number) countObj).longValue() : 0L;
                counts.put(status, count);
            }
        }
        return counts;
    }

    @Override
    public boolean canTransition(Integer currentStatus, Integer targetStatus) {
        if (currentStatus == null) return false;

        // 状态机转换规则
        // 0(待审核) → 1(通过/自动转4待签署) | 2(拒绝) | 3(待补充)
        // 3(待补充) → 0(重新提交) | 2(拒绝)
        // 4(待签署) → 5(已完成)
        // 1, 2, 5 为终态
        switch (currentStatus) {
            case STATUS_PENDING:
                return targetStatus == STATUS_APPROVED
                    || targetStatus == STATUS_REJECTED
                    || targetStatus == STATUS_NEED_MORE;
            case STATUS_NEED_MORE:
                return targetStatus == STATUS_PENDING
                    || targetStatus == STATUS_REJECTED;
            case STATUS_PENDING_SIGN:
                return targetStatus == STATUS_COMPLETED;
            default:
                return false; // 已拒绝(2)、已通过(1)、已完成(5)为终态，不可转换
        }
    }

    /**
     * 当申请被批准时，拒绝同一宠物的其他待审核申请
     */
    private void rejectOtherPendingApplications(Long petId, Long approvedApplicationId) {
        List<AdoptionApplication> pendingApps = applicationMapper.selectList(
                lambdaQuery()
                        .eq(AdoptionApplication::getPetId, petId)
                        .eq(AdoptionApplication::getStatus, STATUS_PENDING)
                        .ne(AdoptionApplication::getId, approvedApplicationId));

        for (AdoptionApplication app : pendingApps) {
            app.setStatus(STATUS_REJECTED);
            app.setReviewComment("该宠物已被其他申请人领养，感谢您的关注");
            app.setReviewTime(new Date());
            applicationMapper.updateById(app);
        }
    }

    /**
     * 填充关联的宠物和申请人信息
     */
    private List<AdoptionApplication> enrichWithDetails(List<AdoptionApplication> applications) {
        if (applications == null || applications.isEmpty()) {
            return applications;
        }

        List<Long> petIds = applications.stream()
                .map(AdoptionApplication::getPetId)
                .distinct()
                .collect(java.util.stream.Collectors.toList());
        List<Long> applicantIds = applications.stream()
                .map(AdoptionApplication::getApplicantId)
                .distinct()
                .collect(java.util.stream.Collectors.toList());

        List<Pet> pets = petIds.isEmpty() ? List.of() :
                petMapper.selectList(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Pet>()
                        .in(Pet::getId, petIds));
        Map<Long, Pet> petMap = pets.stream().collect(java.util.stream.Collectors.toMap(Pet::getId, p -> p));

        List<User> users = applicantIds.isEmpty() ? List.of() :
                userMapper.selectList(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<User>()
                        .in(User::getId, applicantIds));
        Map<Long, User> userMap = users.stream().collect(java.util.stream.Collectors.toMap(User::getId, u -> u));

        for (AdoptionApplication app : applications) {
            Pet pet = petMap.get(app.getPetId());
            if (pet != null) {
                app.setPetName(pet.getName());
                app.setBreed(pet.getBreed());
                app.setPetImage(pet.getImageUrl());
                app.setReason(pet.getDescription());
            }
            User user = userMap.get(app.getApplicantId());
            if (user != null) {
                app.setApplicantName(user.getName());
            }
        }

        return applications;
    }
}
