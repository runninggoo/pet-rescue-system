package com.pet.rescue.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pet.rescue.dto.RegisterRequest;
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
     * 根据条件查询用户列表（带分页，返回总数）
     */
    IPage<User> findUsersByConditionWithPage(Map<String, Object> params, int page, int pageSize);

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

    /**
     * 用户注册
     */
    User register(RegisterRequest request);
}
