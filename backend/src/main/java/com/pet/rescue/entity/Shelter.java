package com.pet.rescue.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.pet.rescue.entity.BaseEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 救助所实体类
 * 对应数据库shelter表
 * 包含上海地区救助所的所有必要信息
 */
@Data
@TableName("shelter")
public class Shelter extends BaseEntity {

    /**
     * 到用户位置的距离（用于智能匹配，不映射到数据库）
     */
    @TableField(exist = false)
    private Double distance;

    /**
     * 救助所名称
     */
    private String name;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 经度（用于距离计算）
     */
    private Double lat;

    /**
     * 纬度（用于距离计算）
     */
    private Double lon;

    /**
     * 最大容纳量
     */
    private Integer maxCapacity;

    /**
     * 当前容纳量
     */
    private Integer currentCapacity;

    /**
     * 上海区域编码
     * 浦东新区：310115
     * 徐汇区：310104
     * 闵行区：310112
     * 静安区：310106
     * 长宁区：310105
     * 其他区域...
     */
    private String regionCode;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 状态（0：正常，1：满员，2：维修中，3：关闭）
     */
    private Integer status;

    /**
     * 医疗等级（0：无医疗，1：基础医疗，2：专业医疗，3：综合医疗）
     */
    private Integer medicalLevel;

    /**
     * 运营描述
     */
    private String description;

    /**
     * 图片URL（多个用逗号分隔）
     */
    private String images;

    /**
     * 是否已入驻系统（0：未入驻，1：已入驻）
     */
    private Integer entryStatus;

    /**
     * 入驻时间
     */
    private java.util.Date entryTime;

    /**
     * 审核状态（0：待审核，1：已通过，2：已拒绝）
     */
    private Integer auditStatus;

    /**
     * 审核意见
     */
    private String auditComment;

    /**
     * 无参构造函数
     */
    public Shelter() {
        this.entryStatus = 0; // 默认未入驻
        this.auditStatus = 0; // 默认待审核
        this.status = 0; // 默认正常
    }

    /**
     * 全参构造函数
     */
    public Shelter(String name, String address, Double lat, Double lon,
            Integer maxCapacity, Integer currentCapacity, String regionCode,
            String phone, Integer medicalLevel, String description) {
        this.name = name;
        this.address = address;
        this.lat = lat;
        this.lon = lon;
        this.maxCapacity = maxCapacity;
        this.currentCapacity = currentCapacity;
        this.regionCode = regionCode;
        this.phone = phone;
        this.medicalLevel = medicalLevel;
        this.description = description;
        this.entryStatus = 0;
        this.auditStatus = 0;
        this.status = 0;
    }

    /**
     * 获取可用容量
     */
    public Integer getAvailableCapacity() {
        return maxCapacity - currentCapacity;
    }

    /**
     * 获取容量使用率
     */
    public Double getCapacityRatio() {
        return (double) currentCapacity / maxCapacity;
    }

    /**
     * 获取容量状态描述
     */
    public String getCapacityStatus() {
        double ratio = getCapacityRatio();
        if (ratio < 0.5)
            return "容量充足";
        else if (ratio < 0.7)
            return "容量适中";
        else if (ratio < 0.9)
            return "容量紧张";
        else
            return "即将满员";
    }

    /**
     * 获取医疗等级描述
     */
    public String getMedicalLevelDesc() {
        switch (medicalLevel) {
            case 0:
                return "无医疗服务";
            case 1:
                return "基础医疗服务";
            case 2:
                return "专业医疗服务";
            case 3:
                return "综合医疗服务";
            default:
                return "未知";
        }
    }

    /**
     * 获取区域名称
     */
    public String getRegionName() {
        switch (regionCode) {
            case "310115":
                return "浦东新区";
            case "310104":
                return "徐汇区";
            case "310112":
                return "闵行区";
            case "310106":
                return "静安区";
            case "310105":
                return "长宁区";
            case "310107":
                return "普陀区";
            case "310108":
                return "闸北区";
            case "310109":
                return "虹口区";
            case "310110":
                return "杨浦区";
            case "310113":
                return "宝山区";
            case "310114":
                return "嘉定区";
            case "310116":
                return "金山区";
            case "310117":
                return "松江区";
            case "310118":
                return "青浦区";
            case "310120":
                return "奉贤区";
            case "310151":
                return "崇明区";
            default:
                return "上海市";
        }
    }

    @Override
    public String toString() {
        return "Shelter{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", regionName='" + getRegionName() + '\'' +
                ", capacity=" + currentCapacity + "/" + maxCapacity +
                ", available=" + getAvailableCapacity() +
                ", status='" + getCapacityStatus() + '\'' +
                '}';
    }
}