package com.pet.rescue.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据范围控制注解
 * 用于Service方法上，控制数据访问范围
 * 
 * @author Claude
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataScope {

    /**
     * 数据范围类型
     * 
     * @return 数据范围类型
     */
    DataScopeType type() default DataScopeType.ALL;

    /**
     * 机构ID字段名（当type为INSTITUTION时使用）
     * 
     * @return 机构ID字段名
     */
    String institutionIdField() default "institutionId";

    /**
     * 用户ID字段名（当type为SELF时使用）
     * 
     * @return 用户ID字段名
     */
    String userIdField() default "userId";
}