package com.pet.rescue.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 映射静态HTML页面和静态资源
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
        // 映射根目录的HTML文件
        registry.addResourceHandler("/*.html")
                .addResourceLocations("classpath:/static/");
    }
}
