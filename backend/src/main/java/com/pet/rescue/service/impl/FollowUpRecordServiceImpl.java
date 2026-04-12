package com.pet.rescue.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pet.rescue.entity.AdoptionApplication;
import com.pet.rescue.entity.FollowUpRecord;
import com.pet.rescue.entity.User;
import com.pet.rescue.mapper.AdoptionApplicationMapper;
import com.pet.rescue.mapper.FollowUpRecordMapper;
import com.pet.rescue.mapper.UserMapper;
import com.pet.rescue.service.FollowUpRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class FollowUpRecordServiceImpl extends ServiceImpl<FollowUpRecordMapper, FollowUpRecord>
        implements FollowUpRecordService {

    private final FollowUpRecordMapper followUpRecordMapper;
    private final UserMapper userMapper;
    private final AdoptionApplicationMapper adoptionApplicationMapper;

    public FollowUpRecordServiceImpl(FollowUpRecordMapper followUpRecordMapper,
                                     UserMapper userMapper,
                                     AdoptionApplicationMapper adoptionApplicationMapper) {
        this.followUpRecordMapper = followUpRecordMapper;
        this.userMapper = userMapper;
        this.adoptionApplicationMapper = adoptionApplicationMapper;
    }

    @Override
    public List<FollowUpRecord> findByAdoptionId(Long adoptionId) {
        List<FollowUpRecord> records = followUpRecordMapper.selectList(
                new LambdaQueryWrapper<FollowUpRecord>()
                        .eq(FollowUpRecord::getAdoptionId, adoptionId)
                        .orderByDesc(FollowUpRecord::getFollowUpDate)
        );
        return enrichWithDetails(records);
    }

    @Override
    public FollowUpRecord findDetail(Long recordId) {
        FollowUpRecord record = followUpRecordMapper.selectById(recordId);
        if (record != null) {
            return enrichWithDetails(List.of(record)).get(0);
        }
        return null;
    }

    @Override
    public boolean addRecord(FollowUpRecord record) {
        if (record.getFollowUpDate() == null) {
            record.setFollowUpDate(new Date());
        }
        return followUpRecordMapper.insert(record) > 0;
    }

    @Override
    public boolean updateRecord(FollowUpRecord record) {
        return followUpRecordMapper.updateById(record) > 0;
    }

    @Override
    public boolean deleteRecord(Long recordId) {
        FollowUpRecord record = followUpRecordMapper.selectById(recordId);
        if (record == null) {
            return false;
        }
        record.setDeleted(1);
        return followUpRecordMapper.updateById(record) > 0;
    }

    /**
     * 填充关联的申请人姓名和宠物信息
     */
    private List<FollowUpRecord> enrichWithDetails(List<FollowUpRecord> records) {
        if (records == null || records.isEmpty()) {
            return records;
        }

        // 收集所有需要关联的 ID
        List<Long> followerIds = records.stream()
                .map(FollowUpRecord::getFollowerId)
                .distinct()
                .collect(Collectors.toList());
        List<Long> adoptionIds = records.stream()
                .map(FollowUpRecord::getAdoptionId)
                .distinct()
                .collect(Collectors.toList());

        // 查询回访人信息
        Map<Long, User> userMap = followerIds.isEmpty() ? Map.of() :
                userMapper.selectList(new LambdaQueryWrapper<User>().in(User::getId, followerIds))
                        .stream().collect(Collectors.toMap(User::getId, u -> u));

        // 查询申请信息（含宠物名和申请人名）
        Map<Long, AdoptionApplication> appMap = adoptionIds.isEmpty() ? Map.of() :
                adoptionApplicationMapper.selectList(new LambdaQueryWrapper<AdoptionApplication>()
                                .in(AdoptionApplication::getId, adoptionIds))
                        .stream().collect(Collectors.toMap(AdoptionApplication::getId, a -> a));

        for (FollowUpRecord record : records) {
            User follower = userMap.get(record.getFollowerId());
            if (follower != null) {
                record.setFollowerName(follower.getName());
            }
            AdoptionApplication app = appMap.get(record.getAdoptionId());
            if (app != null) {
                record.setApplicantName(app.getApplicantName());
                record.setPetName(app.getPetName());
            }
        }

        return records;
    }
}
