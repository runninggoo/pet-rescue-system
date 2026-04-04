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

    /**
     * 逻辑删除标志：0-未删除，1-已删除
     */
    @TableField("deleted")
    private Integer deleted;

    // 扩展字段
    @TableField(exist = false)
    private String applicantName; // 申请人姓名

    @TableField(exist = false)
    private String petName; // 宠物名称

    @TableField(exist = false)
    private String breed; // 宠物品种

    @TableField(exist = false)
    private String petImage; // 宠物图片

    @TableField(exist = false)
    private String reason; // 领养理由

    // Lombok @Data 应该自动生成以下方法，但为确保兼容性手动添加

    public Long getApplicantId() { return applicantId; }
    public void setApplicantId(Long applicantId) { this.applicantId = applicantId; }

    public Long getPetId() { return petId; }
    public void setPetId(Long petId) { this.petId = petId; }

    public Date getApplyDate() { return applyDate; }
    public void setApplyDate(Date applyDate) { this.applyDate = applyDate; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public String getReviewComment() { return reviewComment; }
    public void setReviewComment(String reviewComment) { this.reviewComment = reviewComment; }

    public Date getReviewTime() { return reviewTime; }
    public void setReviewTime(Date reviewTime) { this.reviewTime = reviewTime; }

    public Long getInstitutionId() { return institutionId; }
    public void setInstitutionId(Long institutionId) { this.institutionId = institutionId; }

    public String getFamilyMembers() { return familyMembers; }
    public void setFamilyMembers(String familyMembers) { this.familyMembers = familyMembers; }

    public String getLivingConditions() { return livingConditions; }
    public void setLivingConditions(String livingConditions) { this.livingConditions = livingConditions; }

    public String getWorkStatus() { return workStatus; }
    public void setWorkStatus(String workStatus) { this.workStatus = workStatus; }

    public String getExperience() { return experience; }
    public void setExperience(String experience) { this.experience = experience; }

    public String getApplicantName() { return applicantName; }
    public void setApplicantName(String applicantName) { this.applicantName = applicantName; }

    public String getPetName() { return petName; }
    public void setPetName(String petName) { this.petName = petName; }

    public String getBreed() { return breed; }
    public void setBreed(String breed) { this.breed = breed; }

    public Integer getDeleted() { return deleted; }
    public void setDeleted(Integer deleted) { this.deleted = deleted; }
}