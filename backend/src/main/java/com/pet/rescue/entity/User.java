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
@TableName("user")
public class User extends BaseEntity {

    @TableField("name")
    private String name;

    @TableField("phone")
    private String phone;

    @TableField("password")
    private String password;

    @TableField("role")
    private String role;

    @TableField("status")
    private Integer status;

    /**
     * 逻辑删除标志：0-未删除，1-已删除
     */
    @TableField("deleted")
    private Integer deleted;

    @TableField("email")
    private String email;

    @TableField("address")
    private String address;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // 扩展字段
    @TableField(exist = false)
    private String adopterIdCard; // 领养人身份证

    @TableField(exist = false)
    private String adopterAddress; // 领养人地址

    @TableField(exist = false)
    private String institutionName; // 机构名称

    @TableField(exist = false)
    private String volunteerServiceArea; // 志愿者服务区域

    public Integer getDeleted() { return deleted; }
    public void setDeleted(Integer deleted) { this.deleted = deleted; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    @TableField("age")
    private Integer age;

    @TableField("gender")
    private Integer gender;

    @TableField("birthday")
    private String birthday;

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public Integer getGender() { return gender; }
    public void setGender(Integer gender) { this.gender = gender; }
    public String getBirthday() { return birthday; }
    public void setBirthday(String birthday) { this.birthday = birthday; }
}