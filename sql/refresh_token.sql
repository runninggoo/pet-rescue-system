-- 刷新令牌表：存储用户的Refresh Token
CREATE TABLE IF NOT EXISTS refresh_token (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    token VARCHAR(256) NOT NULL COMMENT '刷新令牌（UUID）',
    device_info VARCHAR(200) COMMENT '设备信息',
    ip_address VARCHAR(50) COMMENT 'IP地址',
    expires_at DATETIME NOT NULL COMMENT '过期时间',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_token (token),
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='刷新令牌表';
