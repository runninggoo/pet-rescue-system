package com.pet.rescue.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pet.rescue.entity.LevelRule;
import com.pet.rescue.entity.PointHistory;
import com.pet.rescue.entity.VolunteerProfile;

import java.util.List;
import java.util.Map;

/**
 * 志愿者成长服务接口
 */
public interface VolunteerGrowthService {

    // ========== 档案相关 ==========

    /**
     * 获取当前用户的成长档案（不存在则创建）
     */
    VolunteerProfile getOrCreateProfile(Long userId);

    /**
     * 获取成长档案（含等级进度信息）
     */
    Map<String, Object> getProfileWithProgress(Long userId);

    /**
     * 更新称号
     */
    void updateTitle(Long userId, String title);

    // ========== 积分相关 ==========

    /**
     * 完成任务时发放积分（由VolunteerTaskServiceImpl调用）
     * @param userId 志愿者用户ID
     * @param taskId 任务ID
     * @param taskTitle 任务标题
     * @param points 积分数量
     * @return 发放结果（含是否升级信息）
     */
    Map<String, Object> awardPointsForTask(Long userId, Long taskId, String taskTitle, Integer points);

    /**
     * 每日签到
     * @return 签到结果
     */
    Map<String, Object> dailySign(Long userId);

    /**
     * 获取积分变动历史（分页）
     */
    Page<PointHistory> getPointHistory(Long userId, int page, int pageSize);

    // ========== 排行榜 ==========

    /**
     * 获取积分排行榜TOP10
     */
    List<Map<String, Object>> getLeaderboard();

    // ========== 等级规则 ==========

    /**
     * 获取所有等级规则
     */
    List<LevelRule> getAllLevelRules();

    // ========== 内部工具 ==========

    /**
     * 根据累计积分计算等级
     */
    Map<String, Object> calculateLevel(Integer totalPoints);
}
