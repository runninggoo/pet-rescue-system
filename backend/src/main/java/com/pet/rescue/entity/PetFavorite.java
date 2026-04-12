package com.pet.rescue.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 宠物收藏实体类
 * 对应数据库 pet_favorite 表
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("pet_favorite")
public class PetFavorite {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 收藏用户ID
     */
    private Long userId;

    /**
     * 被收藏的宠物ID
     */
    private Long petId;

    /**
     * 收藏时间
     */
    @TableField("created_at")
    private Date createdAt;

    /**
     * 更新时间
     */
    @TableField("updated_at")
    private Date updatedTime;

    /**
     * 逻辑删除标志：0-未删除，1-已删除
     */
    @TableField("deleted")
    private Integer deleted;

    // ===== 以下为非数据库字段，通过 enrichWithDetails 填充 =====

    /** 宠物名称（非数据库字段） */
    @TableField(exist = false)
    private String petName;

    /** 宠物品种（非数据库字段） */
    @TableField(exist = false)
    private String petBreed;

    /** 宠物图片URL（非数据库字段） */
    @TableField(exist = false)
    private String petImageUrl;

    /** 宠物状态（非数据库字段）：0-待领养，1-已领养 */
    @TableField(exist = false)
    private Integer petStatus;
}
