package com.pet.rescue.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pet.rescue.entity.PetFavorite;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PetFavoriteMapper extends BaseMapper<PetFavorite> {

    /**
     * 插入或重新激活收藏记录。
     * 使用 MySQL ON DUPLICATE KEY UPDATE 处理唯一键冲突：
     * - 如果 (user_id, pet_id) 唯一键不存在 → INSERT
     * - 如果已存在（无论 deleted=0 还是 deleted=1）→ UPDATE deleted=0, created_at=NOW()
     */
    int insertOrReactivate(@Param("userId") Long userId, @Param("petId") Long petId);
}
