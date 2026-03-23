package com.pet.rescue.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pet.rescue.entity.User;
import com.pet.rescue.mapper.UserMapper;
import com.pet.rescue.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public User findByPhone(String phone) {
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<User> queryWrapper = new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone, phone);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public List<User> findUsersByCondition(Map<String, Object> params) {
        // 使用LambdaQueryWrapper构建动态查询
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<User> queryWrapper = new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
        if (params != null) {
            if (params.containsKey("role")) {
                queryWrapper = queryWrapper.eq(User::getRole, params.get("role"));
            }
            if (params.containsKey("status")) {
                queryWrapper = queryWrapper.eq(User::getStatus, params.get("status"));
            }
            if (params.containsKey("phone")) {
                queryWrapper = queryWrapper.like(User::getPhone, params.get("phone"));
            }
        }
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public boolean updateUserStatus(Long userId, Integer status) {
        User user = new User();
        user.setId(userId);
        user.setStatus(status);
        return baseMapper.updateById(user) > 0;
    }

    @Override
    public User findById(Long userId) {
        return baseMapper.selectById(userId);
    }

    @Override
    public boolean deleteById(Long userId) {
        return baseMapper.deleteById(userId) > 0;
    }
}