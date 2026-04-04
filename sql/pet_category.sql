-- 宠物分类表（品种多级分类）
CREATE TABLE IF NOT EXISTS `pet_category` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    `name` VARCHAR(50) NOT NULL COMMENT '分类名称，如"猫"、"橘猫"',
    `parent_id` BIGINT DEFAULT NULL COMMENT '父级ID，NULL表示一级分类',
    `type` VARCHAR(20) NOT NULL COMMENT '大类：CATS/DOGS/SMALL_ANIMALS/BIRDS/OTHER',
    `sort` INT DEFAULT 0 COMMENT '排序序号',
    `is_active` TINYINT DEFAULT 1 COMMENT '是否启用：0-禁用 1-启用',
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`parent_id`) REFERENCES `pet_category`(`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='宠物分类表';

-- 操作日志表（数据安全审计）
CREATE TABLE IF NOT EXISTS `operation_log` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT COMMENT '操作用户ID',
    `username` VARCHAR(50) COMMENT '操作用户名',
    `operation` VARCHAR(50) NOT NULL COMMENT '操作类型：LOGIN/REGISTER/PET_ADD/PET_UPDATE/PET_DELETE/PET_STATUS/APPLICATION/AUDIT',
    `detail` TEXT COMMENT '操作详情',
    `ip_address` VARCHAR(50) COMMENT 'IP地址',
    `user_role` VARCHAR(20) COMMENT '用户角色',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    KEY `idx_user_id` (`user_id`),
    KEY `idx_operation` (`operation`),
    KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表（数据安全审计）';

-- 为pet表添加category_type字段（宠物大类）
-- 如果字段已存在则忽略
ALTER TABLE `pet` ADD COLUMN IF NOT EXISTS `category_type` VARCHAR(20) DEFAULT NULL COMMENT '宠物大类类型：CATS/DOGS/SMALL_ANIMALS/BIRDS/OTHER' AFTER `urgency_level`;
-- 添加索引
ALTER TABLE `pet` ADD INDEX IF NOT EXISTS `idx_category_type` (`category_type`);

-- 初始数据：一级分类
INSERT INTO `pet_category` (`name`, `parent_id`, `type`, `sort`, `is_active`) VALUES
('猫', NULL, 'CATS', 1, 1),
('狗', NULL, 'DOGS', 2, 1),
('鼠类', NULL, 'SMALL_ANIMALS', 3, 1),
('鸟类', NULL, 'BIRDS', 4, 1),
('其他', NULL, 'OTHER', 5, 1);

-- 二级分类：猫的品种
INSERT INTO `pet_category` (`name`, `parent_id`, `type`, `sort`, `is_active`) VALUES
('中华田园猫', 1, 'CATS', 1, 1),
('英国短毛猫', 1, 'CATS', 2, 1),
('美国短毛猫', 1, 'CATS', 3, 1),
('波斯猫', 1, 'CATS', 4, 1),
('缅因猫', 1, 'CATS', 5, 1),
('布偶猫', 1, 'CATS', 6, 1),
('暹罗猫', 1, 'CATS', 7, 1),
('狸花猫', 1, 'CATS', 8, 1),
('橘猫', 1, 'CATS', 9, 1),
('无毛猫', 1, 'CATS', 10, 1);

-- 二级分类：狗的品种
INSERT INTO `pet_category` (`name`, `parent_id`, `type`, `sort`, `is_active`) VALUES
('金毛寻回犬', 2, 'DOGS', 1, 1),
('拉布拉多', 2, 'DOGS', 2, 1),
('泰迪犬', 2, 'DOGS', 3, 1),
('哈士奇', 2, 'DOGS', 4, 1),
('柯基', 2, 'DOGS', 5, 1),
('柴犬', 2, 'DOGS', 6, 1),
('萨摩耶', 2, 'DOGS', 7, 1),
('边境牧羊犬', 2, 'DOGS', 8, 1),
('中华田园犬', 2, 'DOGS', 9, 1),
('博美', 2, 'DOGS', 10, 1);

-- 二级分类：鼠类
INSERT INTO `pet_category` (`name`, `parent_id`, `type`, `sort`, `is_active`) VALUES
('仓鼠', 3, 'SMALL_ANIMALS', 1, 1),
('豚鼠', 3, 'SMALL_ANIMALS', 2, 1),
('龙猫', 3, 'SMALL_ANIMALS', 3, 1);

-- 二级分类：鸟类
INSERT INTO `pet_category` (`name`, `parent_id`, `type`, `sort`, `is_active`) VALUES
('鹦鹉', 4, 'BIRDS', 1, 1),
('文鸟', 4, 'BIRDS', 2, 1),
('八哥', 4, 'BIRDS', 3, 1);
