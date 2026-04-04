package com.pet.rescue.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 健康档案实体类
 * 对应数据库health_record表
 * 记录宠物的疫苗、体检、治疗等健康相关信息
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("health_record")
public class HealthRecord extends BaseEntity {

    /**
     * 关联宠物ID
     */
    private Long petId;

    /**
     * 记录类型：vaccine-疫苗，checkup-体检，treatment-治疗，other-其他
     */
    private String recordType;

    /**
     * 记录标题
     */
    private String title;

    /**
     * 记录日期
     */
    private Date recordDate;

    /**
     * 记录内容/详情
     */
    private String content;

    /**
     * 执行机构（宠物医院名称）
     */
    private String institution;

    /**
     * 执行人/兽医名称
     */
    private String veterinarian;

    /**
     * 费用（元）
     */
    private Double cost;

    /**
     * 下次提醒日期（用于疫苗提醒等）
     */
    private Date nextReminderDate;

    /**
     * 备注
     */
    private String remark;

    /**
     * 照片URL，多个用逗号分隔
     */
    private String images;

    /**
     * 状态：0-正常，1-已过期，2-已取消
     */
    private Integer status;

    /**
     * 逻辑删除标志：0-未删除，1-已删除
     */
    @TableField("deleted")
    private Integer deleted;

    // ========== 扩展字段（不映射数据库） ==========

    @TableField(exist = false)
    private String petName;

    @TableField(exist = false)
    private String petBreed;

    // ==================== Getter/Setter ====================

    public Long getPetId() { return petId; }
    public void setPetId(Long petId) { this.petId = petId; }

    public String getRecordType() { return recordType; }
    public void setRecordType(String recordType) { this.recordType = recordType; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Date getRecordDate() { return recordDate; }
    public void setRecordDate(Date recordDate) { this.recordDate = recordDate; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getInstitution() { return institution; }
    public void setInstitution(String institution) { this.institution = institution; }

    public String getVeterinarian() { return veterinarian; }
    public void setVeterinarian(String veterinarian) { this.veterinarian = veterinarian; }

    public Double getCost() { return cost; }
    public void setCost(Double cost) { this.cost = cost; }

    public Date getNextReminderDate() { return nextReminderDate; }
    public void setNextReminderDate(Date nextReminderDate) { this.nextReminderDate = nextReminderDate; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    public String getImages() { return images; }
    public void setImages(String images) { this.images = images; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public Integer getDeleted() { return deleted; }
    public void setDeleted(Integer deleted) { this.deleted = deleted; }

    public String getPetName() { return petName; }
    public void setPetName(String petName) { this.petName = petName; }

    public String getPetBreed() { return petBreed; }
    public void setPetBreed(String petBreed) { this.petBreed = petBreed; }

    /**
     * 获取记录类型描述
     */
    public String getRecordTypeDesc() {
        if (recordType == null) return "未知";
        switch (recordType) {
            case "vaccine": return "疫苗接种";
            case "checkup": return "健康体检";
            case "treatment": return "疾病治疗";
            case "surgery": return "手术";
            case "dental": return "牙齿护理";
            case "other": return "其他";
            default: return "未知";
        }
    }
}
