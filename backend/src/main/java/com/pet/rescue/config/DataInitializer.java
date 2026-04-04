package com.pet.rescue.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test")
public class DataInitializer {
    // 暂时禁用数据初始化
}
