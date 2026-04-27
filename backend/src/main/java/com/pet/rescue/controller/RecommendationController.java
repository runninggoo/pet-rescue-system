package com.pet.rescue.controller;

import com.pet.rescue.entity.Pet;
import com.pet.rescue.security.UserContext;
import com.pet.rescue.service.BehaviorRecordService;
import com.pet.rescue.service.RecommendationService;
import com.pet.rescue.vo.ResponseResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 推荐模块Controller
 * 完全独立于原有业务，不影响任何现有接口
 */
@RestController
@RequestMapping("/recommendation")
public class RecommendationController {

    private final BehaviorRecordService behaviorRecordService;
    private final RecommendationService recommendationService;

    public RecommendationController(BehaviorRecordService behaviorRecordService,
                                   RecommendationService recommendationService) {
        this.behaviorRecordService = behaviorRecordService;
        this.recommendationService = recommendationService;
    }

    /**
     * 记录用户行为（静默接口，前端在宠物详情页加载时调用）
     * POST /api/recommendation/record
     */
    @PostMapping("/record")
    public ResponseResult recordBehavior(@RequestBody Map<String, Object> params) {
        try {
            Long userId = UserContext.getCurrentUserId();
            if (userId == null) {
                return ResponseResult.ok("未登录，跳过行为记录");
            }

            Long petId = parseLong(params.get("petId"));
            String behaviorType = (String) params.get("behaviorType");

            if (petId == null || behaviorType == null) {
                return ResponseResult.error("参数不完整");
            }

            behaviorRecordService.recordBehavior(userId, petId, behaviorType);
            return ResponseResult.ok("记录成功");
        } catch (Exception e) {
            return ResponseResult.error("记录失败：" + e.getMessage());
        }
    }

    /**
     * 获取个性化推荐列表
     * GET /api/recommendation/personalized?limit=6
     */
    @GetMapping("/personalized")
    public ResponseResult getPersonalizedRecommendations(
            @RequestParam(defaultValue = "6") int limit) {
        try {
            Long userId = UserContext.getCurrentUserId();

            List<Pet> recommendations;
            if (userId == null) {
                recommendations = recommendationService.getHotPets(limit);
            } else {
                recommendations = recommendationService.getPersonalizedRecommendations(userId, limit);
            }

            return ResponseResult.ok().data(recommendations);
        } catch (Exception e) {
            return ResponseResult.error("获取推荐失败：" + e.getMessage());
        }
    }

    private Long parseLong(Object value) {
        if (value == null) return null;
        if (value instanceof Long) return (Long) value;
        if (value instanceof Integer) return ((Integer) value).longValue();
        if (value instanceof String) {
            try { return Long.parseLong((String) value); } catch (NumberFormatException ignored) {}
        }
        return null;
    }
}
