package com.pet.rescue.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pet.rescue.entity.Pet;
import com.pet.rescue.entity.UserPetBehavior;
import com.pet.rescue.mapper.PetMapper;
import com.pet.rescue.mapper.UserPetBehaviorMapper;
import com.pet.rescue.service.RecommendationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class RecommendationServiceImpl implements RecommendationService {

    private static final double ALPHA = 0.5; // 类别匹配度权重
    private static final double BETA  = 0.3; // 紧急程度权重
    private static final double GAMMA = 0.2; // 新鲜度权重

    private final PetMapper petMapper;
    private final UserPetBehaviorMapper behaviorMapper;

    public RecommendationServiceImpl(PetMapper petMapper, UserPetBehaviorMapper behaviorMapper) {
        this.petMapper = petMapper;
        this.behaviorMapper = behaviorMapper;
    }

    @Override
    public List<Pet> getPersonalizedRecommendations(Long userId, int limit) {
        if (userId == null) {
            return Collections.emptyList();
        }

        // Step 1: 计算用户偏好向量（Top 3 分类）
        Map<String, Double> categoryPreference = computeCategoryPreference(userId);
        if (categoryPreference.isEmpty()) {
            return getHotPets(limit);
        }

        // Step 2: 获取用户已交互的宠物ID集合
        Set<Long> interactedPetIds = getInteractedPetIds(userId);

        // Step 3: 获取候选宠物（待领养 且 未交互）
        LambdaQueryWrapper<Pet> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Pet::getStatus, 0)
               .orderByDesc(Pet::getUrgencyLevel)
               .orderByDesc(Pet::getCreatedTime);
        List<Pet> candidates = petMapper.selectList(wrapper);

        // Step 4: 过滤已交互宠物
        List<Pet> eligible = candidates.stream()
                .filter(p -> !interactedPetIds.contains(p.getId()))
                .collect(Collectors.toList());

        if (eligible.isEmpty()) {
            return getHotPets(limit);
        }

        // Step 5: 加权打分
        List<ScoredPet> scored = eligible.stream()
                .map(pet -> new ScoredPet(pet, scorePet(pet, categoryPreference)))
                .sorted(Comparator.comparingDouble(ScoredPet::getScore).reversed())
                .collect(Collectors.toList());

        // Step 6: 取 Top N
        return scored.stream()
                .limit(limit)
                .map(ScoredPet::getPet)
                .collect(Collectors.toList());
    }

    @Override
    public List<Pet> getHotPets(int limit) {
        LambdaQueryWrapper<Pet> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Pet::getStatus, 0)
               .orderByDesc(Pet::getCreatedTime)
               .last("LIMIT " + limit);
        return petMapper.selectList(wrapper);
    }

    /**
     * 计算用户分类偏好向量
     * 从 user_pet_behavior 表聚合各类别的加权得分
     */
    private Map<String, Double> computeCategoryPreference(Long userId) {
        LambdaQueryWrapper<UserPetBehavior> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserPetBehavior::getUserId, userId);
        List<UserPetBehavior> behaviors = behaviorMapper.selectList(wrapper);

        if (behaviors.isEmpty()) {
            return Collections.emptyMap();
        }

        // 按 categoryType 聚合加权得分
        Map<String, Double> rawScores = new HashMap<>();
        for (UserPetBehavior b : behaviors) {
            double weight = getBehaviorWeight(b.getBehaviorType());
            rawScores.merge(b.getPetId().toString(), weight, Double::sum);
        }

        // petId → categoryType 映射（需查 pet 表）
        if (rawScores.isEmpty()) {
            return Collections.emptyMap();
        }

        List<Pet> pets = petMapper.selectBatchIds(rawScores.keySet().stream()
                .map(Long::parseLong).collect(Collectors.toList()));

        Map<String, Double> categoryScores = new HashMap<>();
        for (Pet pet : pets) {
            if (pet.getCategoryType() != null) {
                Double raw = rawScores.get(pet.getId().toString());
                if (raw != null) {
                    categoryScores.merge(pet.getCategoryType(), raw, Double::sum);
                }
            }
        }

        if (categoryScores.isEmpty()) {
            return Collections.emptyMap();
        }

        // 归一化 → 取 Top 3
        double maxScore = categoryScores.values().stream().mapToDouble(Double::doubleValue).max().orElse(1.0);
        Map<String, Double> normalized = categoryScores.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue() / maxScore
                ));

        // 取 Top 3
        return normalized.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(3)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (a, b) -> a, LinkedHashMap::new));
    }

    /**
     * 获取用户已交互的宠物ID集合
     * 包括：行为记录表 + 收藏表 + 领养申请表
     */
    private Set<Long> getInteractedPetIds(Long userId) {
        Set<Long> ids = new HashSet<>();

        // 行为记录表
        LambdaQueryWrapper<UserPetBehavior> bw = new LambdaQueryWrapper<>();
        bw.eq(UserPetBehavior::getUserId, userId).select(UserPetBehavior::getPetId);
        List<UserPetBehavior> behaviors = behaviorMapper.selectList(bw);
        behaviors.forEach(b -> ids.add(b.getPetId()));

        return ids;
    }

    /**
     * 评分公式：Score = α×类别匹配度 + β×紧急程度 + γ×新鲜度
     */
    private double scorePet(Pet pet, Map<String, Double> categoryPreference) {
        double categoryScore = 0.0;
        String ct = pet.getCategoryType();
        if (ct != null && categoryPreference.containsKey(ct)) {
            categoryScore = categoryPreference.get(ct);
        }

        double urgencyScore = switch (pet.getUrgencyLevel()) {
            case 2 -> 1.0;
            case 1 -> 0.6;
            default -> 0.2;
        };

        double recencyScore;
        if (pet.getCreatedTime() != null) {
            long daysAgo = ChronoUnit.DAYS.between(pet.getCreatedTime().toInstant(), Instant.now());
            recencyScore = daysAgo <= 7 ? 1.0 : (daysAgo <= 30 ? 0.6 : 0.2);
        } else {
            recencyScore = 0.2;
        }

        return ALPHA * categoryScore + BETA * urgencyScore + GAMMA * recencyScore;
    }

    private double getBehaviorWeight(String behaviorType) {
        return switch (behaviorType) {
            case "apply"    -> 5.0;
            case "favorite" -> 3.0;
            case "view"     -> 1.0;
            default         -> 0.0;
        };
    }

    private static class ScoredPet {
        private final Pet pet;
        private final double score;

        ScoredPet(Pet pet, double score) {
            this.pet = pet;
            this.score = score;
        }

        Pet getPet() { return pet; }
        double getScore() { return score; }
    }
}
