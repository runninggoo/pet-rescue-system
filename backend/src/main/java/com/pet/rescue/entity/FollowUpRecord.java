package com.pet.rescue.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pet.rescue.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 回访记录实体类
 * 对应数据库follow_up_record表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("follow_up_record")
public class FollowUpRecord extends BaseEntity {

    /**
     * 关联申请ID
     */
    private Long adoptionId;

    /**
     * 回访日期
     */
    private Date followUpDate;

    /**
     * 回访人用户ID
     */
    private Long followerId;

    /**
     * 回访内容
     */
    private String content;

    /**
     * 宠物现状
     */
    private String petCondition;

    /**
     * 照片URL，多个用逗号分隔
     */
    private String images;

    /**
     * 逻辑删除标志：0-未删除，1-已删除
     */
    @TableField("deleted")
    private Integer deleted;

    // ===== 以下为非数据库字段，通过 enrichWithDetails 填充 =====

    /** 回访人姓名（非数据库字段） */
    @TableField(exist = false)
    private String followerName;

    /** 领养申请人姓名（非数据库字段） */
    @TableField(exist = false)
    private String applicantName;

    /** 宠物名称（非数据库字段） */
    @TableField(exist = false)
    private String petName;
}
