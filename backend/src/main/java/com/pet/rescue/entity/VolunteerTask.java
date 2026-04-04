package com.pet.rescue.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 志愿者任务实体类
 * 对应数据库volunteer_task表
 * 记录系统中的各类志愿任务
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("volunteer_task")
public class VolunteerTask extends BaseEntity {

    /**
     * 任务标题
     */
    private String title;

    /**
     * 任务类型：rescue-救助任务，transport-运输任务，care-照护任务，
     * event-活动支持，medical-医疗协助，other-其他
     */
    private String taskType;

    /**
     * 任务描述
     */
    private String description;

    /**
     * 任务状态：0-待领取，1-进行中，2-已完成，3-已取消
     */
    private Integer status;

    /**
     * 紧急程度：0-普通，1-紧急，2-非常紧急
     */
    private Integer priority;

    /**
     * 关联宠物ID（可选）
     */
    private Long petId;

    /**
     * 关联救助站ID
     */
    private Long shelterId;

    /**
     * 任务地点
     */
    private String location;

    /**
     * 期望完成日期
     */
    private Date expectedDate;

    /**
     * 实际完成日期
     */
    private Date completedDate;

    /**
     * 发布时间
     */
    private Date publishDate;

    /**
     * 任务发布人ID
     */
    private Long publisherId;

    /**
     * 任务接收人/志愿者ID
     */
    private Long volunteerId;

    /**
     * 任务奖励（积分）
     */
    private Integer rewardPoints;

    /**
     * 所需人数
     */
    private Integer requiredPeople;

    /**
     * 已报名人数
     */
    private Integer signedPeople;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 任务耗时（小时）
     */
    private Double estimatedHours;

    /**
     * 任务结果/总结
     */
    private String result;

    /**
     * 逻辑删除标志：0-未删除，1-已删除
     */
    @TableField("deleted")
    private Integer deleted;

    // ========== 扩展字段（不映射数据库） ==========

    @TableField(exist = false)
    private String shelterName;

    @TableField(exist = false)
    private String petName;

    @TableField(exist = false)
    private String publisherName;

    @TableField(exist = false)
    private String volunteerName;

    @TableField(exist = false)
    private String taskTypeDesc;

    // ==================== Getter/Setter ====================

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getTaskType() { return taskType; }
    public void setTaskType(String taskType) { this.taskType = taskType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }

    public Long getPetId() { return petId; }
    public void setPetId(Long petId) { this.petId = petId; }

    public Long getShelterId() { return shelterId; }
    public void setShelterId(Long shelterId) { this.shelterId = shelterId; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Date getExpectedDate() { return expectedDate; }
    public void setExpectedDate(Date expectedDate) { this.expectedDate = expectedDate; }

    public Date getCompletedDate() { return completedDate; }
    public void setCompletedDate(Date completedDate) { this.completedDate = completedDate; }

    public Date getPublishDate() { return publishDate; }
    public void setPublishDate(Date publishDate) { this.publishDate = publishDate; }

    public Long getPublisherId() { return publisherId; }
    public void setPublisherId(Long publisherId) { this.publisherId = publisherId; }

    public Long getVolunteerId() { return volunteerId; }
    public void setVolunteerId(Long volunteerId) { this.volunteerId = volunteerId; }

    public Integer getRewardPoints() { return rewardPoints; }
    public void setRewardPoints(Integer rewardPoints) { this.rewardPoints = rewardPoints; }

    public Integer getRequiredPeople() { return requiredPeople; }
    public void setRequiredPeople(Integer requiredPeople) { this.requiredPeople = requiredPeople; }

    public Integer getSignedPeople() { return signedPeople; }
    public void setSignedPeople(Integer signedPeople) { this.signedPeople = signedPeople; }

    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }

    public Double getEstimatedHours() { return estimatedHours; }
    public void setEstimatedHours(Double estimatedHours) { this.estimatedHours = estimatedHours; }

    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }

    public Integer getDeleted() { return deleted; }
    public void setDeleted(Integer deleted) { this.deleted = deleted; }

    public String getShelterName() { return shelterName; }
    public void setShelterName(String shelterName) { this.shelterName = shelterName; }

    public String getPetName() { return petName; }
    public void setPetName(String petName) { this.petName = petName; }

    public String getPublisherName() { return publisherName; }
    public void setPublisherName(String publisherName) { this.publisherName = publisherName; }

    public String getVolunteerName() { return volunteerName; }
    public void setVolunteerName(String volunteerName) { this.volunteerName = volunteerName; }

    /**
     * 获取任务类型描述
     */
    public String getTaskTypeDesc() {
        if (taskType == null) return "未知";
        switch (taskType) {
            case "rescue": return "救助任务";
            case "transport": return "运输任务";
            case "care": return "照护任务";
            case "event": return "活动支持";
            case "medical": return "医疗协助";
            case "foster": return "临时寄养";
            default: return "其他";
        }
    }

    /**
     * 获取状态描述
     */
    public String getStatusDesc() {
        if (status == null) return "未知";
        switch (status) {
            case 0: return "待领取";
            case 1: return "进行中";
            case 2: return "已完成";
            case 3: return "已取消";
            default: return "未知";
        }
    }

    /**
     * 获取优先级描述
     */
    public String getPriorityDesc() {
        if (priority == null) return "普通";
        switch (priority) {
            case 0: return "普通";
            case 1: return "紧急";
            case 2: return "非常紧急";
            default: return "普通";
        }
    }
}
