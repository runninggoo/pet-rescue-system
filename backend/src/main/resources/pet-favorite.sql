-- ================================================
-- 宠物收藏表
-- 创建时间: 2026-04-12
-- 描述: 记录用户收藏的宠物
-- ================================================
CREATE TABLE IF NOT EXISTS `pet_favorite` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '收藏用户ID',
    `pet_id` BIGINT NOT NULL COMMENT '被收藏的宠物ID',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
    `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除标志：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_pet` (`user_id`, `pet_id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_pet_id` (`pet_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='宠物收藏表';
