package com.pet.rescue.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
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

    /**
     * 宠物大类类型：CATS/DOGS/SMALL_ANIMALS/BIRDS/OTHER（关联pet_category一级分类）
     */
    @TableField("category_type")
    private String categoryType;

    /**
     * 逻辑删除标志：0-未删除，1-已删除
     */
    @TableField("deleted")
    @TableLogic
    private Integer deleted;

    // 扩展字段（不映射到数据库）
    @TableField(exist = false)
    private String institutionName; // 机构名称

    @TableField(exist = false)
    private Double distanceToShelter; // 到推荐救助所的距离

    @TableField(exist = false)
    private String shelterName; // 推荐救助所名称

    // Lombok @Data 应该自动生成以下方法，但为确保兼容性手动添加

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBreed() { return breed; }
    public void setBreed(String breed) { this.breed = breed; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public Integer getGender() { return gender; }
    public void setGender(Integer gender) { this.gender = gender; }

    public BigDecimal getWeight() { return weight; }
    public void setWeight(BigDecimal weight) { this.weight = weight; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getHealthStatus() { return healthStatus; }
    public void setHealthStatus(String healthStatus) { this.healthStatus = healthStatus; }

    public String getPersonality() { return personality; }
    public void setPersonality(String personality) { this.personality = personality; }

    public Date getRescueDate() { return rescueDate; }
    public void setRescueDate(Date rescueDate) { this.rescueDate = rescueDate; }

    public String getRescueLocation() { return rescueLocation; }
    public void setRescueLocation(String rescueLocation) { this.rescueLocation = rescueLocation; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public Long getInstitutionId() { return institutionId; }
    public void setInstitutionId(Long institutionId) { this.institutionId = institutionId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Double getRescueLon() { return rescueLon; }
    public void setRescueLon(Double rescueLon) { this.rescueLon = rescueLon; }

    public Double getRescueLat() { return rescueLat; }
    public void setRescueLat(Double rescueLat) { this.rescueLat = rescueLat; }

    public Long getRecommendedShelterId() { return recommendedShelterId; }
    public void setRecommendedShelterId(Long recommendedShelterId) { this.recommendedShelterId = recommendedShelterId; }

    public Integer getUrgencyLevel() { return urgencyLevel; }
    public void setUrgencyLevel(Integer urgencyLevel) { this.urgencyLevel = urgencyLevel; }

    public Integer getExpectedAdoptionTime() { return expectedAdoptionTime; }
    public void setExpectedAdoptionTime(Integer expectedAdoptionTime) { this.expectedAdoptionTime = expectedAdoptionTime; }

    public String getCategoryType() { return categoryType; }
    public void setCategoryType(String categoryType) { this.categoryType = categoryType; }

    public Integer getDeleted() { return deleted; }
    public void setDeleted(Integer deleted) { this.deleted = deleted; }

    public String getInstitutionName() { return institutionName; }
    public void setInstitutionName(String institutionName) { this.institutionName = institutionName; }

    public Double getDistanceToShelter() { return distanceToShelter; }
    public void setDistanceToShelter(Double distanceToShelter) { this.distanceToShelter = distanceToShelter; }

    public String getShelterName() { return shelterName; }
    public void setShelterName(String shelterName) { this.shelterName = shelterName; }
}