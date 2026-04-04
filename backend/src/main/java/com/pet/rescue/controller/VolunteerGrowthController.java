package com.pet.rescue.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pet.rescue.entity.PointHistory;
import com.pet.rescue.security.UserContext;
import com.pet.rescue.service.VolunteerGrowthService;
import com.pet.rescue.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 志愿者成长控制器
 * 提供积分、等级、排行榜等API
 */
@RestController
@RequestMapping("/volunteer")
public class VolunteerGrowthController {

    private final VolunteerGrowthService volunteerGrowthService;

    @Autowired
    public VolunteerGrowthController(VolunteerGrowthService volunteerGrowthService) {
        this.volunteerGrowthService = volunteerGrowthService;
    }

    /**
     * 获取当前志愿者的成长档案（含等级进度）
     * GET /api/volunteer/profile
     */
    @GetMapping("/profile")
    public ResponseResult getProfile() {
        Long userId = UserContext.getCurrentUserId();
        if (userId == null) {
            return ResponseResult.error("用户未登录");
        }
        Map<String, Object> data = volunteerGrowthService.getProfileWithProgress(userId);
        return ResponseResult.ok().data("profile", data);
    }

    /**
     * 更新志愿者称号
     * PUT /api/volunteer/title
     */
    @PutMapping("/title")
    public ResponseResult updateTitle(@RequestBody Map<String, String> body) {
        Long userId = UserContext.getCurrentUserId();
        if (userId == null) {
            return ResponseResult.error("用户未登录");
        }
        String title = body.get("title");
        if (title == null || title.trim().isEmpty()) {
            return ResponseResult.error("称号不能为空");
        }
        if (title.length() > 20) {
            return ResponseResult.error("称号不能超过20个字符");
        }
        volunteerGrowthService.updateTitle(userId, title.trim());
        return ResponseResult.ok("称号更新成功");
    }

    /**
     * 获取积分变动历史（分页）
     * GET /api/volunteer/point-history?page=1&pageSize=10
     */
    @GetMapping("/point-history")
    public ResponseResult getPointHistory(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        Long userId = UserContext.getCurrentUserId();
        if (userId == null) {
            return ResponseResult.error("用户未登录");
        }
        Page<PointHistory> historyPage = volunteerGrowthService.getPointHistory(userId, page, pageSize);
        ResponseResult result = ResponseResult.ok();
        result.data("history", historyPage.getRecords());
        result.data("total", historyPage.getTotal());
        result.data("page", historyPage.getCurrent());
        result.data("pageSize", historyPage.getSize());
        return result;
    }

    /**
     * 获取积分排行榜TOP10
     * GET /api/volunteer/leaderboard
     */
    @GetMapping("/leaderboard")
    public ResponseResult getLeaderboard() {
        List<Map<String, Object>> leaderboard = volunteerGrowthService.getLeaderboard();
        return ResponseResult.ok().data("leaderboard", leaderboard);
    }

    /**
     * 每日签到
     * POST /api/volunteer/daily-sign
     */
    @PostMapping("/daily-sign")
    public ResponseResult dailySign() {
        Long userId = UserContext.getCurrentUserId();
        if (userId == null) {
            return ResponseResult.error("用户未登录");
        }
        Map<String, Object> result = volunteerGrowthService.dailySign(userId);
        return ResponseResult.ok().data("signResult", result);
    }

    /**
     * 获取所有等级规则
     * GET /api/volunteer/level-rules
     */
    @GetMapping("/level-rules")
    public ResponseResult getLevelRules() {
        return ResponseResult.ok().data("levelRules", volunteerGrowthService.getAllLevelRules());
    }
}
