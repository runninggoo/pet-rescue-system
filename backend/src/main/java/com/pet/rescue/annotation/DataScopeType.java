package com.pet.rescue.annotation;

/**
 * 数据范围类型枚举
 */
public enum DataScopeType {
    /**
     * 全部数据（管理员）
     */
    ALL,

    /**
     * 机构数据（机构管理员）
     */
    INSTITUTION,

    /**
     * 个人数据（普通用户）
     */
    SELF
}
