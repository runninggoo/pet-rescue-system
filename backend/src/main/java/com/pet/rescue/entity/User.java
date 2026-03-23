package com.pet.rescue.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user")
public class User extends BaseEntity {

    @TableField("name")
    private String name;

    @TableField("phone")
    private String phone;

    @TableField("password")
    private String password;

    @TableField("role")
    private String role;

    @TableField("status")
    private Integer status;

    // 扩展字段
    @TableField(exist = false)
    private String adopterIdCard; // 领养人身份证

    @TableField(exist = false)
    private String adopterAddress; // 领养人地址

    @TableField(exist = false)
    private String institutionName; // 机构名称

    @TableField(exist = false)
    private String volunteerServiceArea; // 志愿者服务区域
}