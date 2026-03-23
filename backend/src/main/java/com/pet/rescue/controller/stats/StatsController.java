package com.pet.rescue.controller.stats;

import com.pet.rescue.service.stats.StatsService;
import com.pet.rescue.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    /**
     * 获取救助数量统计
     * @param params 包含startDate, endDate等参数
     * @return 统计结果
     */
    @GetMapping("/rescue-count")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTITUTION_ADMIN')")
    public ResponseResult getRescueCount(@RequestParam Map<String, Object> params) {
        try {
            Map<String, Object> result = statsService.getRescueCount(params);
            return ResponseResult.ok().data("stats", result);
        } catch (Exception e) {
            return ResponseResult.error("获取救助数量统计失败：" + e.getMessage());
        }
    }

    /**
     * 获取领养成功率统计
     * @param params 查询参数
     * @return 统计结果
     */
    @GetMapping("/adoption-rate")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTITUTION_ADMIN')")
    public ResponseResult getAdoptionRate(@RequestParam Map<String, Object> params) {
        try {
            Map<String, Object> result = statsService.getAdoptionRate(params);
            return ResponseResult.ok().data("stats", result);
        } catch (Exception e) {
            return ResponseResult.error("获取领养成功率统计失败：" + e.getMessage());
        }
    }

    /**
     * 获取宠物状态分布统计
     * @return 统计结果
     */
    @GetMapping("/pet-status-distribution")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTITUTION_ADMIN')")
    public ResponseResult getPetStatusDistribution() {
        try {
            Map<String, Object> result = statsService.getPetStatusDistribution();
            return ResponseResult.ok().data("stats", result);
        } catch (Exception e) {
            return ResponseResult.error("获取宠物状态分布统计失败：" + e.getMessage());
        }
    }

    /**
     * 获取机构统计信息
     * @param institutionId 机构ID（可选，管理员查看所有机构）
     * @return 机构统计结果
     */
    @GetMapping("/institution-stats")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTITUTION_ADMIN')")
    public ResponseResult getInstitutionStats(@RequestParam(required = false) Long institutionId) {
        try {
            Map<String, Object> result = statsService.getInstitutionStats(institutionId);
            return ResponseResult.ok().data("stats", result);
        } catch (Exception e) {
            return ResponseResult.error("获取机构统计失败：" + e.getMessage());
        }
    }

    /**
     * 获取回访完成率统计
     * @param params 查询参数
     * @return 统计结果
     */
    @GetMapping("/follow-up-rate")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTITUTION_ADMIN')")
    public ResponseResult getFollowUpRate(@RequestParam Map<String, Object> params) {
        try {
            Map<String, Object> result = statsService.getFollowUpRate(params);
            return ResponseResult.ok().data("stats", result);
        } catch (Exception e) {
            return ResponseResult.error("获取回访完成率统计失败：" + e.getMessage());
        }
    }

    /**
     * 导出统计数据
     * @param params 导出参数
     * @return 导出结果
     */
    @PostMapping("/export")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTITUTION_ADMIN')")
    public ResponseResult exportStats(@RequestBody Map<String, Object> params) {
        try {
            // 这里简化处理，实际应该生成Excel文件
            String exportResult = statsService.exportStats(params);
            return ResponseResult.ok().data("exportResult", exportResult);
        } catch (Exception e) {
            return ResponseResult.error("导出统计数据失败：" + e.getMessage());
        }
    }
}