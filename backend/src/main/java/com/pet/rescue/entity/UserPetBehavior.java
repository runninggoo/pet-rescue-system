package com.pet.rescue.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 用户-宠物行为记录实体类
 * 对应数据库 user_pet_behavior 表
 * 用于采集用户对宠物的行为（浏览/收藏/申请），支撑个性化推荐算法
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user_pet_behavior")
public class UserPetBehavior {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 宠物ID
     */
    private Long petId;

    /**
     * 行为类型：view-浏览（权重1），favorite-收藏（权重3），apply-申请领养（权重5）
     */
    @TableField("behavior_type")
    private String behaviorType;

    /**
     * 行为发生时间
     */
    @TableField("created_at")
    private Date createdAt;
}
