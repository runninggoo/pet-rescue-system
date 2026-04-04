-- ============================================================
-- 志愿者成长体系表结构
-- 包含：志愿者成长档案、积分变动记录、等级规则配置
-- ============================================================

-- ----------------------------
-- 1. 志愿者成长档案表
-- ----------------------------
DROP TABLE IF EXISTS volunteer_profile;
CREATE TABLE volunteer_profile (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    user_id BIGINT NOT NULL COMMENT '用户ID，对应user表',
    total_points INT DEFAULT 0 COMMENT '累计获得积分（只增不减）',
    available_points INT DEFAULT 0 COMMENT '当前可用积分',
    current_level INT DEFAULT 1 COMMENT '当前等级（1-6）',
    current_level_name VARCHAR(20) DEFAULT '新手上路' COMMENT '等级名称',
    total_tasks INT DEFAULT 0 COMMENT '累计完成任务数',
    total_hours DECIMAL(8,2) DEFAULT 0 COMMENT '累计服务时长（小时）',
    title VARCHAR(50) COMMENT '专属称号（可自定义）',
    continuous_sign_days INT DEFAULT 0 COMMENT '连续签到天数',
    last_sign_date DATE COMMENT '最后签到日期',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    UNIQUE KEY uk_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='志愿者成长档案表';

-- ----------------------------
-- 2. 积分变动记录表
-- ----------------------------
DROP TABLE IF EXISTS point_history;
CREATE TABLE point_history (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    points INT NOT NULL COMMENT '积分变动数量（正数=获得，负数=消费）',
    balance INT NOT NULL COMMENT '变动后可用积分余额',
    source VARCHAR(30) NOT NULL COMMENT '来源类型',
    related_id BIGINT COMMENT '关联ID（如任务ID、签到ID等）',
    description VARCHAR(200) COMMENT '变动描述',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='积分变动记录表';

-- source 可选值：
-- TASK_COMPLETE        - 完成任务获得
-- TASK_BONUS           - 任务完成奖励
-- DAILY_SIGN           - 每日签到
-- CONTINUOUS_SIGN_7    - 连续签到7天奖励
-- LEVEL_UP             - 等级提升奖励
-- TITLE_CHANGE         - 称号变更

-- ----------------------------
-- 3. 等级成长规则表（固定配置，无需修改）
-- ----------------------------
DROP TABLE IF EXISTS level_rule;
CREATE TABLE level_rule (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    level INT NOT NULL COMMENT '等级（1-6）',
    level_name VARCHAR(20) NOT NULL COMMENT '等级名称',
    min_points INT NOT NULL COMMENT '所需累计积分（不含本级）',
    max_points INT DEFAULT 99999 COMMENT '本级积分上限',
    badge_icon VARCHAR(10) DEFAULT '⭐' COMMENT '徽章图标',
    privilege_desc VARCHAR(200) COMMENT '等级特权描述',
    level_color VARCHAR(20) DEFAULT '#E07A5F' COMMENT '等级主题色',
    sort_order INT DEFAULT 0 COMMENT '排序序号'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='等级成长规则表';

-- 插入6个等级配置
INSERT INTO level_rule (level, level_name, min_points, max_points, badge_icon, privilege_desc, level_color, sort_order) VALUES
(1, '新手上路',    0,    99,   '🌱', '可领取基础任务',                         '#81B29A', 1),
(2, '小小志愿者',  100,  299,  '⭐', '可领取普通任务，提前参与活动',            '#F4A261', 2),
(3, '爱心使者',   300,  599,  '🌟', '可领取紧急任务，获得称号定制权限',        '#E07A5F', 3),
(4, '救助达人',   600,  999,  '🏅', '可领取医疗任务，优先获得稀有任务推送',   '#CE93D8', 4),
(5, '金牌志愿者', 1000, 1999, '🥇', '可领取所有任务，参与年度志愿者评选',      '#FDCB6E', 5),
(6, '公益之星',   2000, 99999,'🏆', '最高荣誉认证，获得专属志愿者证书',        '#FF6B6B', 6);

-- ----------------------------
-- 4. 初始化志愿者档案（为现有志愿者创建档案）
-- ----------------------------
-- 为所有志愿者角色用户初始化成长档案
INSERT INTO volunteer_profile (user_id, total_points, available_points, current_level, current_level_name, total_tasks, total_hours)
SELECT u.id, 0, 0, 1, '新手上路', 0, 0
FROM user u
WHERE u.role = 'volunteer'
  AND u.deleted = 0
  AND NOT EXISTS (SELECT 1 FROM volunteer_profile vp WHERE vp.user_id = u.id);

-- ----------------------------
-- 5. 为现有已完成的任务补充积分记录
-- ----------------------------
-- 为已完成的任务（status=2）补充积分记录
INSERT INTO point_history (user_id, points, balance, source, related_id, description)
SELECT
    vt.volunteer_id,
    COALESCE(vt.reward_points, 20),
    COALESCE(vt.reward_points, 20),
    'TASK_COMPLETE',
    vt.id,
    CONCAT('完成任务：', vt.title)
FROM volunteer_task vt
WHERE vt.status = 2
  AND vt.volunteer_id IS NOT NULL
  AND NOT EXISTS (
      SELECT 1 FROM point_history ph WHERE ph.source = 'TASK_COMPLETE' AND ph.related_id = vt.id
  );

-- 同时更新志愿者档案中的累计积分
UPDATE volunteer_profile vp
INNER JOIN (
    SELECT vt.volunteer_id,
           SUM(COALESCE(vt.reward_points, 20)) AS earned_points,
           COUNT(*) AS task_count
    FROM volunteer_task vt
    WHERE vt.status = 2
      AND vt.volunteer_id IS NOT NULL
    GROUP BY vt.volunteer_id
) AS task_stats ON vp.user_id = task_stats.volunteer_id
SET
    vp.total_points = task_stats.earned_points,
    vp.available_points = task_stats.earned_points,
    vp.total_tasks = task_stats.task_count,
    vp.current_level = CASE
        WHEN task_stats.earned_points >= 2000 THEN 6
        WHEN task_stats.earned_points >= 1000 THEN 5
        WHEN task_stats.earned_points >= 600 THEN 4
        WHEN task_stats.earned_points >= 300 THEN 3
        WHEN task_stats.earned_points >= 100 THEN 2
        ELSE 1
    END,
    vp.current_level_name = CASE
        WHEN task_stats.earned_points >= 2000 THEN '公益之星'
        WHEN task_stats.earned_points >= 1000 THEN '金牌志愿者'
        WHEN task_stats.earned_points >= 600 THEN '救助达人'
        WHEN task_stats.earned_points >= 300 THEN '爱心使者'
        WHEN task_stats.earned_points >= 100 THEN '小小志愿者'
        ELSE '新手上路'
    END;
