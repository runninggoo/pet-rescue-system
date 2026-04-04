package com.pet.rescue.controller;

import com.pet.rescue.dto.LoginRequest;
import com.pet.rescue.dto.RegisterRequest;
import com.pet.rescue.entity.User;
import com.pet.rescue.service.OperationLogService;
import com.pet.rescue.service.UserService;
import com.pet.rescue.utils.JwtTokenUtil;
import com.pet.rescue.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final OperationLogService operationLogService;

    public AuthController(JwtTokenUtil jwtTokenUtil,
            UserService userService,
            @Lazy BCryptPasswordEncoder passwordEncoder,
            OperationLogService operationLogService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.operationLogService = operationLogService;
    }

    private String getIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public ResponseResult login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        try {
            // 直接通过UserService验证用户
            User user = userService.findByPhone(loginRequest.getPhone());
            if (user == null) {
                return ResponseResult.error("用户不存在");
            }

            // 验证密码
            if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                return ResponseResult.error("密码错误");
            }

            // 记录登录日志
            operationLogService.logOperation(user.getId(), user.getName(), "LOGIN",
                    "用户登录成功，角色：" + user.getRole(),
                    getIp(request), user.getRole());

            // 密码验证成功，生成JWT令牌
            String jwt = jwtTokenUtil.generateToken(user.getPhone(), user.getId(), user.getRole());

            // 返回token和用户信息（data方法支持链式调用，合并到同一对象）
            return ResponseResult.ok().data("token", jwt).data("user", user);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("登录失败：" + e.getMessage());
        }
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public ResponseResult register(@RequestBody RegisterRequest request, HttpServletRequest httpRequest) {
        try {
            // 验证两次密码一致
            if (!request.getPassword().equals(request.getConfirmPassword())) {
                return ResponseResult.error("两次输入的密码不一致");
            }
            // 验证密码长度
            if (request.getPassword().length() < 6) {
                return ResponseResult.error("密码长度不能少于6位");
            }
            // 验证手机号格式
            if (!request.getPhone().matches("^1[3-9]\\d{9}$")) {
                return ResponseResult.error("请输入正确的手机号");
            }
            // 验证姓名
            if (request.getName() == null || request.getName().trim().isEmpty()) {
                return ResponseResult.error("请输入真实姓名");
            }
            // 检查手机号是否已存在
            User existingUser = userService.findByPhone(request.getPhone());
            if (existingUser != null) {
                return ResponseResult.error("该手机号已注册，请直接登录");
            }
            // 注册用户
            User newUser = userService.register(request);

            // 记录注册日志
            operationLogService.logOperation(newUser.getId(), newUser.getName(), "REGISTER",
                    "新用户注册，角色：" + newUser.getRole() + "，手机号：" + newUser.getPhone(),
                    getIp(httpRequest), newUser.getRole());

            // 注册成功后自动登录，生成Token
            String jwt = jwtTokenUtil.generateToken(newUser.getPhone(), newUser.getId(), newUser.getRole());
            return ResponseResult.ok("注册成功").data("token", jwt).data("user", newUser);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("注册失败：" + e.getMessage());
        }
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/current")
    public ResponseResult getCurrentUser() {
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return ResponseResult.ok().data("user", user);
        } catch (Exception e) {
            return ResponseResult.error("获取用户信息失败");
        }
    }

}