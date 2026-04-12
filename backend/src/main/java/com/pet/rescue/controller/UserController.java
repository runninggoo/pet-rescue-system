package com.pet.rescue.controller;

import com.pet.rescue.entity.User;
import com.pet.rescue.service.UserService;
import com.pet.rescue.vo.ResponseResult;
import com.pet.rescue.security.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserController(UserService userService, @Lazy BCryptPasswordEncoder passwordEncoder) {
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
            Long currentUserId = getCurrentUserId();
            String role = getCurrentUserRole();

            if ("institution_admin".equals(role)) {
                params.put("institutionId", currentUserId);
                params.put("notAdmin", true);
            }

            int page = 1;
            int pageSize = 12;
            try {
                if (params.containsKey("page") && params.get("page") != null) {
                    page = Integer.parseInt(params.get("page").toString());
                }
                if (params.containsKey("pageSize") && params.get("pageSize") != null) {
                    pageSize = Integer.parseInt(params.get("pageSize").toString());
                }
            } catch (NumberFormatException ignored) {}

            var pageResult = userService.findUsersByConditionWithPage(params, page, pageSize);
            return ResponseResult.ok()
                    .data("users", pageResult.getRecords())
                    .data("total", pageResult.getTotal());
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
    public ResponseResult updateStatus(@PathVariable Long userId, @RequestBody Map<String, Integer> body) {
        try {
            Integer status = body.get("status");
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
     * 获取当前用户个人信息（需登录）
     */
    @GetMapping("/profile")
    public ResponseResult getProfile() {
        try {
            User user = UserContext.getCurrentUser();
            if (user != null) {
                return ResponseResult.ok().data("user", user);
            } else {
                return ResponseResult.error("未登录");
            }
        } catch (Exception e) {
            return ResponseResult.error("获取个人信息失败：" + e.getMessage());
        }
    }

    /**
     * 更新当前用户个人信息（需登录，只能修改自己）
     */
    @PutMapping("/profile")
    public ResponseResult updateProfile(@RequestBody User userUpdate) {
        try {
            User currentUser = UserContext.getCurrentUser();
            if (currentUser == null) {
                return ResponseResult.error("未登录");
            }

            // 只允许更新部分字段，防止越权
            currentUser.setName(userUpdate.getName());
            currentUser.setEmail(userUpdate.getEmail());
            currentUser.setAddress(userUpdate.getAddress());
            if (userUpdate.getAge() != null) currentUser.setAge(userUpdate.getAge());
            if (userUpdate.getGender() != null) currentUser.setGender(userUpdate.getGender());
            if (userUpdate.getBirthday() != null) currentUser.setBirthday(userUpdate.getBirthday());

            // 密码单独处理
            if (userUpdate.getPassword() != null && !userUpdate.getPassword().isEmpty()) {
                currentUser.setPassword(passwordEncoder.encode(userUpdate.getPassword()));
            }

            boolean success = userService.updateById(currentUser);
            if (success) {
                return ResponseResult.ok("更新成功");
            } else {
                return ResponseResult.error("更新失败");
            }
        } catch (Exception e) {
            return ResponseResult.error("更新个人信息失败：" + e.getMessage());
        }
    }

    /**
     * 获取当前用户ID（辅助方法）
     */
    private Long getCurrentUserId() {
        return UserContext.getCurrentUserId();
    }

    /**
     * 获取当前用户角色（辅助方法）
     */
    private String getCurrentUserRole() {
        return UserContext.getCurrentUserRole();
    }
}