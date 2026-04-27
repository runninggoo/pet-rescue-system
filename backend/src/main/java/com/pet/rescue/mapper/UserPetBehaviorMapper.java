package com.pet.rescue.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pet.rescue.entity.UserPetBehavior;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 用户-宠物行为记录Mapper
 */
@Repository
public interface UserPetBehaviorMapper extends BaseMapper<UserPetBehavior> {

    /**
     * 插入行为记录。
     * 联合唯一键 (user_id, pet_id, behavior_type) 保证同一用户对同一宠物的同一行为不重复写入。
     * ON DUPLICATE KEY UPDATE 仅更新时间戳，实现幂等性。
     */
    int insertOrIgnore(@Param("userId") Long userId,
                      @Param("petId") Long petId,
                      @Param("behaviorType") String behaviorType);
}
