package com.pet.rescue.controller;

import com.pet.rescue.entity.User;
import com.pet.rescue.service.UserService;
import com.pet.rescue.vo.ResponseResult;
import com.pet.rescue.security.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserController(UserService userService, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 获取用户列表（根据角色过滤）
     */
    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTITUTION_ADMIN')")
    public ResponseResult list(@RequestParam Map<String, Object> params) {
        try {
            // 简化权限控制
            Long currentUserId = getCurrentUserId();
            String role = getCurrentUserRole();

            if ("institution_admin".equals(role)) {
                // 机构管理员只能查看自己机构的用户
                params.put("institutionId", currentUserId);
                params.put("notAdmin", true); // 不能查看管理员
            }
            // 管理员可以查看所有用户

            List<User> users = userService.findUsersByCondition(params);
            return ResponseResult.ok().data("users", users);
        } catch (Exception e) {
            return ResponseResult.error("获取用户列表失败：" + e.getMessage());
        }
    }

    /**
     * 添加用户（管理员）
     */
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseResult add(@RequestBody User user) {
        try {
            // 检查手机号是否已存在
            User existingUser = userService.findByPhone(user.getPhone());
            if (existingUser != null) {
                return ResponseResult.error("该手机号已注册");
            }

            // 加密密码
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setStatus(1); // 默认启用

            // 保存用户
            boolean success = userService.save(user);
            if (success) {
                return ResponseResult.ok("添加用户成功");
            } else {
                return ResponseResult.error("添加用户失败");
            }
        } catch (Exception e) {
            return ResponseResult.error("添加用户失败：" + e.getMessage());
        }
    }

    /**
     * 更新用户信息（管理员）
     */
    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseResult update(@RequestBody User user) {
        try {
            boolean success = userService.updateById(user);
            if (success) {
                return ResponseResult.ok("更新用户成功");
            } else {
                return ResponseResult.error("更新用户失败");
            }
        } catch (Exception e) {
            return ResponseResult.error("更新用户失败：" + e.getMessage());
        }
    }

    /**
     * 删除用户（管理员）
     */
    @DeleteMapping("/delete/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseResult delete(@PathVariable Long userId) {
        try {
            boolean success = userService.deleteById(userId);
            if (success) {
                return ResponseResult.ok("删除用户成功");
            } else {
                return ResponseResult.error("删除用户失败");
            }
        } catch (Exception e) {
            return ResponseResult.error("删除用户失败：" + e.getMessage());
        }
    }

    /**
     * 更新用户状态（管理员）
     */
    @PutMapping("/status/{userId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTITUTION_ADMIN')")
    public ResponseResult updateStatus(@PathVariable Long userId, @RequestParam Integer status) {
        try {
            boolean success = userService.updateUserStatus(userId, status);
            if (success) {
                return ResponseResult.ok("更新状态成功");
            } else {
                return ResponseResult.error("更新状态失败");
            }
        } catch (Exception e) {
            return ResponseResult.error("更新状态失败：" + e.getMessage());
        }
    }

    /**
     * 获取用户详情
     */
    @GetMapping("/detail/{userId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('INSTITUTION_ADMIN')")
    public ResponseResult detail(@PathVariable Long userId) {
        try {
            User user = userService.findById(userId);
            if (user != null) {
                return ResponseResult.ok().data("user", user);
            } else {
                return ResponseResult.error("用户不存在");
            }
        } catch (Exception e) {
            return ResponseResult.error("获取用户详情失败：" + e.getMessage());
        }
    }

    /**
     * 获取当前用户ID（辅助方法）
     */
    private Long getCurrentUserId() {
        // 实际项目中应该从安全上下文中获取
        // 这里简化处理，实际使用时需要完善
        return UserContext.getCurrentUserId(); // 使用UserContext获取用户ID
    }

    /**
     * 获取当前用户角色（辅助方法）
     */
    private String getCurrentUserRole() {
        // 实际项目中应该从安全上下文中获取
        // 这里简化处理，实际使用时需要完善
        return UserContext.getCurrentUserRole(); // 使用UserContext获取用户角色
    }
}