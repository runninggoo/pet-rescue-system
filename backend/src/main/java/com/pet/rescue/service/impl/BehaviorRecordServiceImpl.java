package com.pet.rescue.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pet.rescue.entity.UserPetBehavior;
import com.pet.rescue.mapper.UserPetBehaviorMapper;
import com.pet.rescue.service.BehaviorRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BehaviorRecordServiceImpl implements BehaviorRecordService {

    private final UserPetBehaviorMapper behaviorMapper;

    public BehaviorRecordServiceImpl(UserPetBehaviorMapper behaviorMapper) {
        this.behaviorMapper = behaviorMapper;
    }

    @Override
    public boolean recordBehavior(Long userId, Long petId, String behaviorType) {
        if (userId == null || petId == null || behaviorType == null) {
            return false;
        }
        int rows = behaviorMapper.insertOrIgnore(userId, petId, behaviorType);
        return rows > 0;
    }
}
