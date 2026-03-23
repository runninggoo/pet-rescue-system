package com.pet.rescue.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pet.rescue.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService extends IService<User> {
    User findByPhone(String phone);

    /**
     * 根据条件查询用户列表（带权限过滤）
     */
    List<User> findUsersByCondition(Map<String, Object> params);

    /**
     * 更新用户状态
     */
    boolean updateUserStatus(Long userId, Integer status);

    /**
     * 根据ID查询用户详情
     */
    User findById(Long userId);

    /**
     * 根据ID删除用户
     */
    boolean deleteById(Long userId);
}