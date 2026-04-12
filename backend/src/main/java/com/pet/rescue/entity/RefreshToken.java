package com.pet.rescue.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("refresh_token")
public class RefreshToken {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String token;

    private String deviceInfo;

    private String ipAddress;

    private Date expiresAt;

    private Date createdAt;
}
