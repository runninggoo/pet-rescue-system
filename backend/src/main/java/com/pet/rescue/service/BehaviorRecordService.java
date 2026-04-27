package com.pet.rescue.service;

import com.pet.rescue.entity.Pet;
import java.util.List;

/**
 * 行为记录服务接口
 * 负责采集用户对宠物的行为（浏览/收藏/申请）
 */
public interface BehaviorRecordService {

    /**
     * 记录用户行为（幂等写入，同一用户+宠物+行为类型不重复记录）
     * @param userId 用户ID
     * @param petId  宠物ID
     * @param behaviorType 行为类型：view / favorite / apply
     * @return true-记录成功，false-无需重复记录
     */
    boolean recordBehavior(Long userId, Long petId, String behaviorType);
}
