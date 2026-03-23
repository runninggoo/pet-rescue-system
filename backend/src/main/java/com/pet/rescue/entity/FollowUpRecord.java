package com.pet.rescue.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.pet.rescue.entity.BaseEntity;
import java.util.Date;

/**
 * 回访记录实体类
 * 对应数据库follow_up_record表
 */
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
     * 无参构造函数
     */
    public FollowUpRecord() {
    }

    /**
     * 全参构造函数
     */
    public FollowUpRecord(Long adoptionId, Date followUpDate, Long followerId,
                         String content, String petCondition, String images) {
        this.adoptionId = adoptionId;
        this.followUpDate = followUpDate;
        this.followerId = followerId;
        this.content = content;
        this.petCondition = petCondition;
        this.images = images;
    }

    // Getter和Setter方法
    public Long getAdoptionId() {
        return adoptionId;
    }

    public void setAdoptionId(Long adoptionId) {
        this.adoptionId = adoptionId;
    }

    public Date getFollowUpDate() {
        return followUpDate;
    }

    public void setFollowUpDate(Date followUpDate) {
        this.followUpDate = followUpDate;
    }

    public Long getFollowerId() {
        return followerId;
    }

    public void setFollowerId(Long followerId) {
        this.followerId = followerId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPetCondition() {
        return petCondition;
    }

    public void setPetCondition(String petCondition) {
        this.petCondition = petCondition;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "FollowUpRecord{" +
                "adoptionId=" + adoptionId +
                ", followUpDate=" + followUpDate +
                ", followerId=" + followerId +
                ", content='" + content + '\'' +
                ", petCondition='" + petCondition + '\'' +
                ", images='" + images + '\'' +
                "} " + super.toString();
    }
}