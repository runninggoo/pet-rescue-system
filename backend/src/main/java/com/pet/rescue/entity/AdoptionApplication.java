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
@TableName("adoption_application")
public class AdoptionApplication extends BaseEntity {

    @TableField("applicant_id")
    private Long applicantId; // 申请人user_id

    @TableField("pet_id")
    private Long petId; // 宠物id

    @TableField("apply_date")
    private Date applyDate; // 申请时间

    @TableField("status")
    private Integer status; // 0待审核，1通过，2拒绝，3待补充，4待签署，5已完成

    @TableField("review_comment")
    private String reviewComment; // 审核意见

    @TableField("review_time")
    private Date reviewTime; // 审核时间

    @TableField("institution_id")
    private Long institutionId; // 机构ID

    @TableField("family_members")
    private String familyMembers; // 家庭成员意见

    @TableField("living_conditions")
    private String livingConditions; // 居住条件

    @TableField("work_status")
    private String workStatus; // 工作状况

    @TableField("experience")
    private String experience; // 养宠经验

    // 扩展字段
    @TableField(exist = false)
    private String applicantName; // 申请人姓名

    @TableField(exist = false)
    private String petName; // 宠物名称

    @TableField(exist = false)
    private String breed; // 宠物品种
}