package com.pet.rescue.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis Plus 配置
 *
 * 策略：覆盖 auto-config 注册的 MybatisPlusInterceptor。
 * 只注册 PaginationInnerInterceptor，但设置 overflow=true，
 * 跳过无法被 JSqlParser 解析的 SQL（如自定义 @Select SQL）。
 * 配合 ServiceImpl 中的手动分页逻辑，所有分页查询正常工作。
 */
@Configuration
public class MyBatisPlusConfig {

    @Bean
    @ConditionalOnMissingBean(MybatisPlusInterceptor.class)
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor();
        // overflow=true：当查询结果数量超过 LIMIT 时，自动变为查询全部（不抛异常）
        paginationInterceptor.setOverflow(true);
        // 设置最大单页限制，防止恶意查询
        paginationInterceptor.setMaxLimit(500L);

        interceptor.addInnerInterceptor(paginationInterceptor);
        return interceptor;
    }
}
