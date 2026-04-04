package com.pet.rescue.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("operation_log")
public class OperationLog {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 用户ID */
    @TableField("user_id")
    private Long userId;

    /** 用户名 */
    @TableField("username")
    private String username;

    /** 操作类型：LOGIN/LOGOUT/APPLICATION/AUDIT/PET_ADD/PET_UPDATE/PET_DELETE/CATEGORY... */
    @TableField("operation")
    private String operation;

    /** 操作详情JSON */
    @TableField("detail")
    private String detail;

    /** IP地址 */
    @TableField("ip_address")
    private String ipAddress;

    /** 用户角色 */
    @TableField("user_role")
    private String userRole;

    /** 创建时间 */
    @TableField("created_at")
    private Date createdAt;
}
