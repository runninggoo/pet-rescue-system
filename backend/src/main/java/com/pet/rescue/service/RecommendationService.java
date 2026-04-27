package com.pet.rescue.service;

import com.pet.rescue.entity.Pet;
import java.util.List;

/**
 * 个性化推荐服务接口
 * 基于用户行为偏好向量，对未浏览的宠物加权打分排序后推荐
 */
public interface RecommendationService {

    /**
     * 获取个性化推荐列表
     * @param userId 用户ID（可为null，返回热门兜底列表）
     * @param limit  返回数量上限
     * @return 推荐宠物列表（按推荐得分降序）
     */
    List<Pet> getPersonalizedRecommendations(Long userId, int limit);

    /**
     * 获取热门宠物列表（兜底推荐）
     * @param limit 返回数量上限
     * @return 热门宠物列表
     */
    List<Pet> getHotPets(int limit);
}
