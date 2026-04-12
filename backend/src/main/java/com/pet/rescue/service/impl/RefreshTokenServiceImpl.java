package com.pet.rescue.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.pet.rescue.entity.RefreshToken;
import com.pet.rescue.entity.User;
import com.pet.rescue.mapper.RefreshTokenMapper;
import com.pet.rescue.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Autowired
    private RefreshTokenMapper refreshTokenMapper;

    @Value("${jwt.refresh-expiration:604800000}")
    private Long refreshExpiration; // 默认7天

    @Override
    public RefreshToken createRefreshToken(User user, String deviceInfo, String ipAddress) {
        // 先删除该用户的旧令牌（单设备登录模式）
        deleteByUserId(user.getId());

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUserId(user.getId());
        refreshToken.setToken(UUID.randomUUID().toString().replace("-", ""));
        refreshToken.setDeviceInfo(deviceInfo);
        refreshToken.setIpAddress(ipAddress);
        refreshToken.setExpiresAt(Date.from(LocalDateTime.now()
                .plusSeconds(refreshExpiration.longValue() / 1000)
                .atZone(ZoneId.systemDefault())
                .toInstant()));
        refreshToken.setCreatedAt(new Date());

        refreshTokenMapper.insert(refreshToken);
        return refreshToken;
    }

    @Override
    public RefreshToken findByToken(String token) {
        LambdaQueryWrapper<RefreshToken> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RefreshToken::getToken, token);
        return refreshTokenMapper.selectOne(wrapper);
    }

    @Override
    public boolean validateRefreshToken(String token) {
        RefreshToken refreshToken = findByToken(token);
        if (refreshToken == null) {
            return false;
        }
        if (refreshToken.getExpiresAt().before(new Date())) {
            // 已过期，删除
            deleteByToken(token);
            return false;
        }
        return true;
    }

    @Override
    public void deleteByUserId(Long userId) {
        LambdaQueryWrapper<RefreshToken> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RefreshToken::getUserId, userId);
        refreshTokenMapper.delete(wrapper);
    }

    @Override
    public void deleteByToken(String token) {
        LambdaQueryWrapper<RefreshToken> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RefreshToken::getToken, token);
        refreshTokenMapper.delete(wrapper);
    }

    @Override
    @Scheduled(cron = "0 0 3 * * ?") // 每天凌晨3点清理
    public void cleanExpiredTokens() {
        LambdaUpdateWrapper<RefreshToken> wrapper = new LambdaUpdateWrapper<>();
        wrapper.lt(RefreshToken::getExpiresAt, new Date());
        refreshTokenMapper.delete(wrapper);
    }
}
