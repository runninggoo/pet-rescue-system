package com.pet.rescue.config;

import com.pet.rescue.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .cors().and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .authorizeRequests()
                // 认证相关路径（登录/注册/获取当前用户）允许所有人访问
                .antMatchers("/auth/**").permitAll()
                // 宠物分类/品种API：宠物列表无需登录，品种下拉应公开访问
                .antMatchers("/category/**").permitAll()
                // 救助站推荐API允许所有人访问（便于搜索功能）
                .antMatchers("/shelter-recommendation/**").permitAll()
                // 宠物详情API：宠物详情页是公开页面，API也应允许匿名访问
                .antMatchers("/pet/detail/**").permitAll()
                // 静态资源（图片、JS、CSS等）
                .antMatchers("/static/**", "/css/**", "/js/**", "/images/**").permitAll()
                // 公开页面：宠物列表/详情、救助站推荐页、登录、注册
                .antMatchers(
                    "/pet-list.html", "/pet-detail.html",
                    "/shelter-recommendation.html",
                    "/login.html", "/register.html"
                ).permitAll()
                // 个人信息页：需登录（所有角色均可）
                .antMatchers("/profile.html").authenticated()
                // 管理员专属页面
                .antMatchers("/user-management.html").hasAnyRole("ADMIN")
                .antMatchers("/simple-stats.html").hasAnyRole("ADMIN", "INSTITUTION_ADMIN")
                // 机构管理员专属页面
                .antMatchers("/stats.html").hasAnyRole("ADMIN", "INSTITUTION_ADMIN")
                // 领养申请页：管理员/机构管理员可访问，领养人只能看自己
                .antMatchers("/adoption-list.html").hasAnyRole("ADMIN", "INSTITUTION_ADMIN", "ADOPTER")
                // 医疗档案页：管理员/机构管理员/宠物医院
                .antMatchers("/health-record.html", "/pet-hospital.html").hasAnyRole("ADMIN", "INSTITUTION_ADMIN", "PET_HOSPITAL")
                // 志愿者任务页：管理员/机构管理员/志愿者可访问
                .antMatchers("/volunteer-task.html", "/task-list.html").hasAnyRole("ADMIN", "INSTITUTION_ADMIN", "VOLUNTEER")
                // 其他所有请求需要认证
                .anyRequest().authenticated()
            .and()
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}