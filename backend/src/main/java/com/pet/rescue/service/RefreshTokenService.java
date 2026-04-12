package com.pet.rescue.service;

import com.pet.rescue.entity.RefreshToken;
import com.pet.rescue.entity.User;

public interface RefreshTokenService {

    /**
     * 为用户生成新的刷新令牌
     */
    RefreshToken createRefreshToken(User user, String deviceInfo, String ipAddress);

    /**
     * 根据token字符串查找刷新令牌
     */
    RefreshToken findByToken(String token);

    /**
     * 验证刷新令牌是否有效
     */
    boolean validateRefreshToken(String token);

    /**
     * 删除用户的刷新令牌
     */
    void deleteByUserId(Long userId);

    /**
     * 删除指定token
     */
    void deleteByToken(String token);

    /**
     * 清理过期令牌
     */
    void cleanExpiredTokens();
}
