package com.pet.rescue.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pet")
public class Pet extends BaseEntity {

    @TableField("name")
    private String name;

    @TableField("breed")
    private String breed;

    @TableField("age")
    private Integer age; // 年龄（月）

    @TableField("gender")
    private Integer gender; // 0未知，1公，2母

    @TableField("weight")
    private BigDecimal weight; // 体重（kg）

    @TableField("color")
    private String color; // 毛色

    @TableField("health_status")
    private String healthStatus; // 健康状况

    @TableField("personality")
    private String personality; // 性格特征

    @TableField("rescue_date")
    private Date rescueDate; // 救助日期

    @TableField("rescue_location")
    private String rescueLocation; // 救助地点

    @TableField("image_url")
    private String imageUrl; // 图片URL

    @TableField("status")
    private Integer status; // 0待领养，1已领养，2下架

    @TableField("institution_id")
    private Long institutionId; // 发布机构管理员user_id

    @TableField("description")
    private String description; // 详细描述

    /**
     * 救助地点经度（用于智能匹配）
     */
    @TableField("rescue_lon")
    private Double rescueLon;

    /**
     * 救助地点纬度（用于智能匹配）
     */
    @TableField("rescue_lat")
    private Double rescueLat;

    /**
     * 推荐救助所ID（智能匹配结果）
     */
    @TableField("recommended_shelter_id")
    private Long recommendedShelterId;

    /**
     * 紧急程度（0：一般，1：紧急，2：非常紧急）
     */
    @TableField("urgency_level")
    private Integer urgencyLevel;

    /**
     * 预计领养时间（0：不确定，1：一周内，2：一个月内，3：三个月内）
     */
    @TableField("expected_adoption_time")
    private Integer expectedAdoptionTime;

    // 扩展字段（不映射到数据库）
    @TableField(exist = false)
    private String institutionName; // 机构名称

    @TableField(exist = false)
    private Double distanceToShelter; // 到推荐救助所的距离

    @TableField(exist = false)
    private String shelterName; // 推荐救助所名称
}