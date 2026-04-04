package com.pet.rescue.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 志愿者成长档案实体
 */
@Data
@TableName("volunteer_profile")
public class VolunteerProfile {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Integer totalPoints;

    private Integer availablePoints;

    private Integer currentLevel;

    private String currentLevelName;

    private Integer totalTasks;

    private BigDecimal totalHours;

    private String title;

    private Integer continuousSignDays;

    private Date lastSignDate;

    private Date createdAt;

    private Date updatedAt;

    private Integer deleted;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Integer getTotalPoints() { return totalPoints; }
    public void setTotalPoints(Integer totalPoints) { this.totalPoints = totalPoints; }
    public Integer getAvailablePoints() { return availablePoints; }
    public void setAvailablePoints(Integer availablePoints) { this.availablePoints = availablePoints; }
    public Integer getCurrentLevel() { return currentLevel; }
    public void setCurrentLevel(Integer currentLevel) { this.currentLevel = currentLevel; }
    public String getCurrentLevelName() { return currentLevelName; }
    public void setCurrentLevelName(String currentLevelName) { this.currentLevelName = currentLevelName; }
    public Integer getTotalTasks() { return totalTasks; }
    public void setTotalTasks(Integer totalTasks) { this.totalTasks = totalTasks; }
    public BigDecimal getTotalHours() { return totalHours; }
    public void setTotalHours(BigDecimal totalHours) { this.totalHours = totalHours; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Integer getContinuousSignDays() { return continuousSignDays; }
    public void setContinuousSignDays(Integer continuousSignDays) { this.continuousSignDays = continuousSignDays; }
    public Date getLastSignDate() { return lastSignDate; }
    public void setLastSignDate(Date lastSignDate) { this.lastSignDate = lastSignDate; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
    public Integer getDeleted() { return deleted; }
    public void setDeleted(Integer deleted) { this.deleted = deleted; }
}
