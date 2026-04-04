package com.pet.rescue.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pet.rescue.entity.LevelRule;
import com.pet.rescue.entity.PointHistory;
import com.pet.rescue.entity.VolunteerProfile;
import com.pet.rescue.mapper.LevelRuleMapper;
import com.pet.rescue.mapper.PointHistoryMapper;
import com.pet.rescue.mapper.VolunteerProfileMapper;
import com.pet.rescue.service.VolunteerGrowthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * 志愿者成长服务实现
 */
@Service
public class VolunteerGrowthServiceImpl implements VolunteerGrowthService {

    private final VolunteerProfileMapper profileMapper;
    private final PointHistoryMapper pointHistoryMapper;
    private final LevelRuleMapper levelRuleMapper;

    // 固定等级规则（与数据库 level_rule 表一致）
    private static final Object[][] LEVEL_THRESHOLDS = {
        {1, 0,    99,   "新手上路",    "🌱", "#81B29A"},
        {2, 100,  299,  "小小志愿者",  "⭐", "#F4A261"},
        {3, 300,  599,  "爱心使者",    "🌟", "#E07A5F"},
        {4, 600,  999,  "救助达人",    "🏅", "#CE93D8"},
        {5, 1000, 1999, "金牌志愿者",  "🥇", "#FDCB6E"},
        {6, 2000, 99999,"公益之星",    "🏆", "#FF6B6B"}
    };

    // 签到积分配置
    private static final int DAILY_SIGN_POINTS = 5;
    private static final int CONTINUOUS_SIGN_BONUS_DAYS = 7;
    private static final int CONTINUOUS_SIGN_BONUS_POINTS = 20;

    @Autowired
    public VolunteerGrowthServiceImpl(VolunteerProfileMapper profileMapper,
                                       PointHistoryMapper pointHistoryMapper,
                                       LevelRuleMapper levelRuleMapper) {
        this.profileMapper = profileMapper;
        this.pointHistoryMapper = pointHistoryMapper;
        this.levelRuleMapper = levelRuleMapper;
    }

    @Override
    public VolunteerProfile getOrCreateProfile(Long userId) {
        LambdaQueryWrapper<VolunteerProfile> q = new LambdaQueryWrapper<>();
        q.eq(VolunteerProfile::getUserId, userId);
        VolunteerProfile profile = profileMapper.selectOne(q);
        if (profile == null) {
            profile = new VolunteerProfile();
            profile.setUserId(userId);
            profile.setTotalPoints(0);
            profile.setAvailablePoints(0);
            profile.setCurrentLevel(1);
            profile.setCurrentLevelName("新手上路");
            profile.setTotalTasks(0);
            profile.setTotalHours(BigDecimal.ZERO);
            profile.setContinuousSignDays(0);
            profile.setLastSignDate(null);
            profile.setDeleted(0);
            profileMapper.insert(profile);
        }
        return profile;
    }

    @Override
    public Map<String, Object> getProfileWithProgress(Long userId) {
        VolunteerProfile profile = getOrCreateProfile(userId);
        Map<String, Object> levelInfo = calculateLevel(profile.getTotalPoints());

        // 获取下一等级信息
        int currentLevel = profile.getCurrentLevel();
        Object[] nextLevelInfo = null;
        for (Object[] rule : LEVEL_THRESHOLDS) {
            if (((Number) rule[0]).intValue() == currentLevel + 1 && currentLevel < 6) {
                nextLevelInfo = rule;
                break;
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("profile", profile);
        result.put("currentLevelInfo", levelInfo);
        result.put("nextLevelPoints", nextLevelInfo != null ? ((Number) nextLevelInfo[1]).intValue() : null);
        result.put("nextLevelName", nextLevelInfo != null ? nextLevelInfo[3] : null);
        result.put("nextLevelBadge", nextLevelInfo != null ? nextLevelInfo[4] : null);
        result.put("isTopLevel", currentLevel >= 6);

        return result;
    }

    @Override
    public void updateTitle(Long userId, String title) {
        VolunteerProfile profile = getOrCreateProfile(userId);
        profile.setTitle(title);
        profileMapper.updateById(profile);
    }

    @Override
    @Transactional
    public Map<String, Object> awardPointsForTask(Long userId, Long taskId, String taskTitle, Integer points) {
        VolunteerProfile profile = getOrCreateProfile(userId);
        int oldLevel = profile.getCurrentLevel();
        int oldPoints = profile.getTotalPoints() != null ? profile.getTotalPoints() : 0;

        // 更新档案
        profile.setTotalPoints(oldPoints + points);
        profile.setAvailablePoints((profile.getAvailablePoints() != null ? profile.getAvailablePoints() : 0) + points);
        profile.setTotalTasks((profile.getTotalTasks() != null ? profile.getTotalTasks() : 0) + 1);

        // 重新计算等级
        Map<String, Object> levelInfo = calculateLevel(profile.getTotalPoints());
        int newLevel = (int) levelInfo.get("level");
        profile.setCurrentLevel(newLevel);
        profile.setCurrentLevelName((String) levelInfo.get("levelName"));
        profileMapper.updateById(profile);

        // 记录积分历史
        PointHistory history = new PointHistory();
        history.setUserId(userId);
        history.setPoints(points);
        history.setBalance(profile.getAvailablePoints());
        history.setSource("TASK_COMPLETE");
        history.setRelatedId(taskId);
        history.setDescription("完成任务：" + taskTitle);
        pointHistoryMapper.insert(history);

        // 构建返回
        Map<String, Object> result = new HashMap<>();
        result.put("pointsEarned", points);
        result.put("newTotalPoints", profile.getTotalPoints());
        result.put("newAvailablePoints", profile.getAvailablePoints());
        result.put("levelInfo", levelInfo);
        result.put("leveledUp", newLevel > oldLevel);
        result.put("oldLevel", oldLevel);
        result.put("newLevel", newLevel);
        if (newLevel > oldLevel) {
            result.put("levelUpMessage", "恭喜升至Lv." + newLevel + " " + levelInfo.get("levelName") + "！");
        }

        return result;
    }

    @Override
    @Transactional
    public Map<String, Object> dailySign(Long userId) {
        VolunteerProfile profile = getOrCreateProfile(userId);
        LocalDate today = LocalDate.now();

        // 检查今天是否已签到
        if (profile.getLastSignDate() != null) {
            LocalDate lastSign = profile.getLastSignDate().toInstant()
                    .atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            if (!lastSign.isBefore(today)) {
                Map<String, Object> result = new HashMap<>();
                result.put("success", false);
                result.put("message", "今日已签到，明天再来吧！");
                return result;
            }
        }

        // 计算连续签到
        int continuousDays = profile.getContinuousSignDays() != null ? profile.getContinuousSignDays() : 0;
        LocalDate yesterday = today.minusDays(1);
        if (profile.getLastSignDate() != null) {
            LocalDate lastSign = profile.getLastSignDate().toInstant()
                    .atZone(java.time.ZoneId.systemDefault()).toLocalDate();
            if (lastSign.equals(yesterday)) {
                continuousDays++;
            } else {
                continuousDays = 1;
            }
        } else {
            continuousDays = 1;
        }

        // 计算本次积分
        int earnedPoints = DAILY_SIGN_POINTS;
        String description = "每日签到";
        if (continuousDays == CONTINUOUS_SIGN_BONUS_DAYS) {
            earnedPoints += CONTINUOUS_SIGN_BONUS_POINTS;
            description = "连续签到" + CONTINUOUS_SIGN_BONUS_DAYS + "天奖励！";
        }

        // 更新档案
        int oldPoints = profile.getTotalPoints() != null ? profile.getTotalPoints() : 0;
        profile.setTotalPoints(oldPoints + earnedPoints);
        profile.setAvailablePoints((profile.getAvailablePoints() != null ? profile.getAvailablePoints() : 0) + earnedPoints);
        profile.setContinuousSignDays(continuousDays);
        profile.setLastSignDate(java.sql.Date.valueOf(today));
        profileMapper.updateById(profile);

        // 记录积分历史
        PointHistory history = new PointHistory();
        history.setUserId(userId);
        history.setPoints(earnedPoints);
        history.setBalance(profile.getAvailablePoints());
        history.setSource(continuousDays == CONTINUOUS_SIGN_BONUS_DAYS ? "CONTINUOUS_SIGN_7" : "DAILY_SIGN");
        history.setDescription(description);
        pointHistoryMapper.insert(history);

        // 构建返回
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", continuousDays == CONTINUOUS_SIGN_BONUS_DAYS
                ? "连续签到" + CONTINUOUS_SIGN_BONUS_DAYS + "天！获得" + earnedPoints + "积分（含20积分奖励）！"
                : "签到成功！获得" + earnedPoints + "积分！");
        result.put("pointsEarned", earnedPoints);
        result.put("continuousDays", continuousDays);
        result.put("continuousBonus", continuousDays == CONTINUOUS_SIGN_BONUS_DAYS);
        result.put("newTotalPoints", profile.getTotalPoints());
        result.put("newAvailablePoints", profile.getAvailablePoints());

        return result;
    }

    @Override
    public Page<PointHistory> getPointHistory(Long userId, int page, int pageSize) {
        Page<PointHistory> p = new Page<>(page, pageSize);
        LambdaQueryWrapper<PointHistory> q = new LambdaQueryWrapper<>();
        q.eq(PointHistory::getUserId, userId).orderByDesc(PointHistory::getCreatedAt);
        return pointHistoryMapper.selectPage(p, q);
    }

    @Override
    public List<Map<String, Object>> getLeaderboard() {
        LambdaQueryWrapper<VolunteerProfile> q = new LambdaQueryWrapper<>();
        q.orderByDesc(VolunteerProfile::getTotalPoints)
         .last("LIMIT 10");
        List<VolunteerProfile> profiles = profileMapper.selectList(q);

        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < profiles.size(); i++) {
            VolunteerProfile p = profiles.get(i);
            Map<String, Object> item = new HashMap<>();
            item.put("rank", i + 1);
            item.put("userId", p.getUserId());
            item.put("totalPoints", p.getTotalPoints());
            item.put("level", p.getCurrentLevel());
            item.put("levelName", p.getCurrentLevelName());
            item.put("title", p.getTitle());
            item.put("totalTasks", p.getTotalTasks());
            list.add(item);
        }
        return list;
    }

    @Override
    public List<LevelRule> getAllLevelRules() {
        return levelRuleMapper.selectAllOrderByLevel();
    }

    @Override
    public Map<String, Object> calculateLevel(Integer totalPoints) {
        if (totalPoints == null) totalPoints = 0;
        Object[] matched = LEVEL_THRESHOLDS[0];
        for (Object[] rule : LEVEL_THRESHOLDS) {
            if (totalPoints >= ((Number) rule[1]).intValue()) {
                matched = rule;
            } else {
                break;
            }
        }
        Map<String, Object> info = new HashMap<>();
        info.put("level", ((Number) matched[0]).intValue());
        info.put("levelName", (String) matched[3]);
        info.put("badge", (String) matched[4]);
        info.put("color", (String) matched[5]);
        info.put("minPoints", ((Number) matched[1]).intValue());
        info.put("maxPoints", ((Number) matched[2]).intValue());
        return info;
    }
}
