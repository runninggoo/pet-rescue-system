package com.pet.rescue.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pet.rescue.entity.PetFavorite;

import java.util.List;

public interface PetFavoriteService {

    /**
     * 添加收藏
     * @param userId 用户ID
     * @param petId 宠物ID
     * @return true-收藏成功，false-已收藏过
     */
    boolean addFavorite(Long userId, Long petId);

    /**
     * 取消收藏
     * @param userId 用户ID
     * @param petId 宠物ID
     * @return true-取消成功，false-未收藏
     */
    boolean removeFavorite(Long userId, Long petId);

    /**
     * 检查用户是否收藏了指定宠物
     * @param userId 用户ID
     * @param petId 宠物ID
     * @return true-已收藏，false-未收藏
     */
    boolean isFavorited(Long userId, Long petId);

    /**
     * 获取用户的收藏列表
     * @param userId 用户ID
     * @param page 页码
     * @param pageSize 每页数量
     * @return 收藏列表（分页）
     */
    Page<PetFavorite> getUserFavorites(Long userId, int page, int pageSize);

    /**
     * 获取用户收藏的所有宠物ID列表
     * @param userId 用户ID
     * @return 宠物ID列表
     */
    List<Long> getUserFavoritePetIds(Long userId);
}
