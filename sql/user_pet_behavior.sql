CREATE TABLE IF NOT EXISTS `user_pet_behavior` (
    `id`           BIGINT       NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
    `user_id`      BIGINT       NOT NULL                 COMMENT 'User ID',
    `pet_id`       BIGINT       NOT NULL                 COMMENT 'Pet ID',
    `behavior_type` VARCHAR(20) NOT NULL                 COMMENT 'Behavior type: view/favorite/apply',
    `created_at`   DATETIME     NOT NULL DEFAULT NOW()   COMMENT 'Behavior timestamp',

    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_pet_behavior` (`user_id`, `pet_id`, `behavior_type`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='User pet behavior record';
