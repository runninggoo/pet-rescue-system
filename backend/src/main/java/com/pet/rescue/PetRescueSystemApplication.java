package com.pet.rescue;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 宠物救助收养系统启动类
 *
 * MybatisPlusAutoConfiguration 不再被排除，
 * SqlSessionFactory 由 auto-config 正常创建。
 * 分页拦截器问题通过 MyBatisPlusConfig 中的自定义配置解决。
 */
@SpringBootApplication
@MapperScan("com.pet.rescue.mapper")
@EnableScheduling
public class PetRescueSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(PetRescueSystemApplication.class, args);
    }
}
