package com.pet.rescue.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

@Data
@TableName("pet_category")
public class PetCategory {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 分类名称，如"猫"、"橘猫" */
    @TableField("name")
    private String name;

    /** 父级ID，NULL表示一级分类 */
    @TableField("parent_id")
    private Long parentId;

    /** 大类：CATS/DOGS/SMALL_ANIMALS/BIRDS/OTHER */
    @TableField("type")
    private String type;

    /** 排序序号 */
    @TableField("sort")
    private Integer sort;

    /** 是否启用：0-禁用 1-启用 */
    @TableField("is_active")
    private Integer isActive;

    /** 子分类（非数据库字段） */
    @TableField(exist = false)
    private List<PetCategory> children;
}
