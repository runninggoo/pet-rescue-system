package com.pet.rescue.controller;

import com.pet.rescue.dto.LoginRequest;
import com.pet.rescue.entity.User;
import com.pet.rescue.service.UserService;
import com.pet.rescue.security.UserDetailsServiceImpl;
import com.pet.rescue.utils.JwtTokenUtil;
import com.pet.rescue.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager,
            JwtTokenUtil jwtTokenUtil,
            UserService userService,
            BCryptPasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public ResponseResult login(@RequestBody LoginRequest loginRequest) {
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

            // 密码验证成功，生成JWT令牌
            String jwt = jwtTokenUtil.generateToken(user.getPhone(), user.getId(), user.getRole());

            // 返回token
            return ResponseResult.ok().data("token", jwt);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseResult.error("登录失败：" + e.getMessage());
        }
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public ResponseResult register(@RequestBody User user) {
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
            userService.save(user);

            return ResponseResult.ok("注册成功");
        } catch (Exception e) {
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