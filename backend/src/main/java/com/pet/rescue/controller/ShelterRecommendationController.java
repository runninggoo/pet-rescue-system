package com.pet.rescue.controller;

import com.pet.rescue.dto.ShelterRecommendationRequest;
import com.pet.rescue.service.ShelterService;
import com.pet.rescue.vo.ResponseResult;
import com.pet.rescue.vo.ShelterRecommendationVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 救助站推荐Controller
 * 提供智能推荐附近可用救助站的API接口
 */
@RestController
@RequestMapping("/shelter-recommendation")
@CrossOrigin(origins = "*")
public class ShelterRecommendationController {

    private final ShelterService shelterService;

    public ShelterRecommendationController(ShelterService shelterService) {
        this.shelterService = shelterService;
    }

    /**
     * 智能推荐附近可用的救助站
     */
    @PostMapping("/recommend")
    public ResponseResult recommendShelters(@RequestBody ShelterRecommendationRequest request) {

        // 参数校验
        if (request.getLatitude() == null || request.getLongitude() == null) {
            return ResponseResult.error(400, "位置信息不能为空");
        }

        if (request.getMaxDistance() == null) {
            request.setMaxDistance(10.0); // 默认10公里
        }

        if (request.getLimit() == null) {
            request.setLimit(10); // 默认返回10个
        }

        if (request.getNeedCapacity() == null) {
            request.setNeedCapacity(true); // 默认只返回有容量的
        }

        try {
            List<ShelterRecommendationVO> recommendations = shelterService.recommendShelters(request);
            return ResponseResult.ok().data("data", recommendations);
        } catch (IllegalArgumentException e) {
            return ResponseResult.error(400, e.getMessage());
        } catch (Exception e) {
            return ResponseResult.error(500, "推荐失败：" + e.getMessage());
        }
    }

    /**
     * 测试接口
     */
    @GetMapping("/test")
    public ResponseResult test() {
        return ResponseResult.ok("救助站推荐服务正常运行");
    }
}
