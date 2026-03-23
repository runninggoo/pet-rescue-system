package com.pet.rescue.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pet.rescue.entity.AdoptionApplication;
import com.pet.rescue.mapper.AdoptionApplicationMapper;
import com.pet.rescue.service.AdoptionApplicationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class AdoptionApplicationServiceImpl extends ServiceImpl<AdoptionApplicationMapper, AdoptionApplication>
        implements AdoptionApplicationService {

    private final AdoptionApplicationMapper applicationMapper;

    public AdoptionApplicationServiceImpl(AdoptionApplicationMapper applicationMapper) {
        this.applicationMapper = applicationMapper;
    }

    @Override
    public boolean submitApplication(AdoptionApplication application) {
        // 检查是否已申请
        if (hasApplied(application.getPetId(), application.getApplicantId())) {
            throw new RuntimeException("您已对该宠物提交过申请");
        }

        // 设置默认状态和时间
        application.setStatus(0); // 待审核
        application.setApplyDate(new java.util.Date());
        return applicationMapper.insert(application) > 0;
    }

    @Override
    public List<AdoptionApplication> findByApplicantId(Long applicantId) {
        return applicationMapper.selectList(
                lambdaQuery().eq(AdoptionApplication::getApplicantId, applicantId));
    }

    @Override
    public List<AdoptionApplication> findByPetId(Long petId) {
        return applicationMapper.selectList(
                lambdaQuery().eq(AdoptionApplication::getPetId, petId));
    }

    @Override
    public List<AdoptionApplication> findByStatus(Integer status) {
        return applicationMapper.selectList(
                lambdaQuery().eq(AdoptionApplication::getStatus, status));
    }

    @Override
    public boolean reviewApplication(Long applicationId, Integer status, String reviewComment) {
        AdoptionApplication application = applicationMapper.selectById(applicationId);
        if (application == null) {
            throw new RuntimeException("申请记录不存在");
        }

        application.setStatus(status);
        application.setReviewComment(reviewComment);
        application.setReviewTime(new java.util.Date());
        return applicationMapper.updateById(application) > 0;
    }

    @Override
    public boolean hasApplied(Long petId, Long applicantId) {
        return applicationMapper.findByPetIdAndApplicantId(petId, applicantId) != null;
    }

    @Override
    public boolean updateStatus(Long applicationId, Integer status) {
        AdoptionApplication application = new AdoptionApplication();
        application.setId(applicationId);
        application.setStatus(status);
        return applicationMapper.updateById(application) > 0;
    }

    /**
     * 根据条件查询申请列表（带权限过滤）
     */
    @Override
    public List<AdoptionApplication> findByCondition(Map<String, Object> params) {
        // 获取查询参数
        Integer status = params.containsKey("status") ? (Integer) params.get("status") : null;
        Long institutionId = params.containsKey("institutionId") ? (Long) params.get("institutionId") : null;

        // 基础查询：按状态查询
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<AdoptionApplication> queryWrapper = new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();

        if (status != null) {
            queryWrapper = queryWrapper.eq(AdoptionApplication::getStatus, status);
        }

        // 权限过滤：机构管理员只能查看自己机构的申请
        if (institutionId != null) {
            queryWrapper = queryWrapper.eq(AdoptionApplication::getInstitutionId, institutionId);
        }

        return applicationMapper.selectList(queryWrapper);
    }
}