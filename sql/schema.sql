-- 宠物救助收养系统数据库脚本
-- 优化后的数据库设计

-- 用户表
CREATE TABLE `user` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `name` varchar(100) NOT NULL COMMENT '姓名',
    `phone` varchar(20) NOT NULL COMMENT '手机号（登录账号）',
    `password` varchar(100) NOT NULL COMMENT '加密密码',
    `role` varchar(20) NOT NULL COMMENT '角色：adopter/institution_admin/volunteer/hospital/admin',
    `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '0禁用，1正常',
    `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 机构表
CREATE TABLE `institution` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `name` varchar(100) NOT NULL COMMENT '机构名称',
    `address` varchar(200) COMMENT '地址',
    `phone` varchar(20) COMMENT '联系电话',
    `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '0禁用，1正常',
    `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='救助机构表';

-- 机构管理员扩展表
CREATE TABLE `institution_admin` (
    `user_id` bigint(20) NOT NULL,
    `institution_id` bigint(20) NOT NULL COMMENT '所属机构ID',
    PRIMARY KEY (`user_id`),
    FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    FOREIGN KEY (`institution_id`) REFERENCES `institution` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='机构管理员扩展表';

-- 领养人扩展表
CREATE TABLE `adopter` (
    `user_id` bigint(20) NOT NULL,
    `id_card` varchar(18) COMMENT '身份证号',
    `address` varchar(200) COMMENT '家庭地址',
    `experience` text COMMENT '养宠经验',
    `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`user_id`),
    FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='领养人扩展表';

-- 志愿者扩展表
CREATE TABLE `volunteer` (
    `user_id` bigint(20) NOT NULL,
    `service_area` varchar(100) COMMENT '服务区域',
    `task_types` varchar(100) COMMENT '可接任务类型（逗号分隔）',
    `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`user_id`),
    FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='志愿者扩展表';

-- 宠物医院扩展表
CREATE TABLE `hospital` (
    `user_id` bigint(20) NOT NULL,
    `hospital_name` varchar(100) NOT NULL COMMENT '医院名称',
    `address` varchar(200) COMMENT '地址',
    `license` varchar(100) COMMENT '资质证书编号',
    `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`user_id`),
    FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='宠物医院扩展表';

-- 救助所表（上海地区特色功能）
CREATE TABLE `shelter` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `name` varchar(100) NOT NULL COMMENT '救助所名称',
    `address` varchar(200) NOT NULL COMMENT '详细地址',
    `lat` decimal(10, 8) NOT NULL COMMENT '纬度（用于距离计算）',
    `lon` decimal(11, 8) NOT NULL COMMENT '经度（用于距离计算）',
    `max_capacity` int(11) NOT NULL COMMENT '最大容纳量',
    `current_capacity` int(11) NOT NULL DEFAULT '0' COMMENT '当前容纳量',
    `region_code` varchar(10) NOT NULL COMMENT '上海区域编码（如：310115浦东新区）',
    `phone` varchar(20) COMMENT '联系电话',
    `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态：0正常，1满员，2维修中，3关闭',
    `medical_level` tinyint(1) NOT NULL DEFAULT '0' COMMENT '医疗等级：0无医疗，1基础医疗，2专业医疗，3综合医疗',
    `description` text COMMENT '运营描述',
    `images` varchar(500) COMMENT '图片URL（多个用逗号分隔）',
    `entry_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '入驻状态：0未入驻，1已入驻',
    `entry_time` datetime COMMENT '入驻时间',
    `audit_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '审核状态：0待审核，1已通过，2已拒绝',
    `audit_comment` varchar(500) COMMENT '审核意见',
    `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_region` (`region_code`),
    KEY `idx_status` (`status`),
    KEY `idx_entry_status` (`entry_status`),
    KEY `idx_audit_status` (`audit_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='救助所表（上海地区特色）';

-- 宠物表（优化后）
CREATE TABLE `pet` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `deleted` int(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除标志：0-未删除，1-已删除',
    `name` varchar(50) NOT NULL COMMENT '宠物名称',
    `breed` varchar(50) NOT NULL COMMENT '品种',
    `age` int(11) NOT NULL COMMENT '年龄（月）',
    `gender` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0未知，1公，2母',
    `weight` decimal(5,2) COMMENT '体重（kg）',
    `color` varchar(50) COMMENT '毛色',
    `health_status` varchar(200) NOT NULL COMMENT '健康状况',
    `personality` text COMMENT '性格特征',
    `rescue_date` date NOT NULL COMMENT '救助日期',
    `rescue_location` varchar(200) NOT NULL COMMENT '救助地点',
    `image_url` varchar(255) COMMENT '图片URL',
    `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0待领养，1已领养，2下架',
    `institution_id` bigint(20) NOT NULL COMMENT '发布机构管理员user_id',
    `description` text COMMENT '详细描述',
    `rescue_lon` decimal(11, 8) COMMENT '救助地点经度（用于智能匹配）',
    `rescue_lat` decimal(10, 8) COMMENT '救助地点纬度（用于智能匹配）',
    `recommended_shelter_id` bigint(20) COMMENT '推荐救助所ID（智能匹配结果）',
    `urgency_level` tinyint(1) NOT NULL DEFAULT '0' COMMENT '紧急程度：0一般，1紧急，2非常紧急',
    `expected_adoption_time` tinyint(1) NOT NULL DEFAULT '0' COMMENT '预计领养时间：0不确定，1一周内，2一个月内，3三个月内',
    `category_type` varchar(20) DEFAULT NULL COMMENT '宠物大类类型：CATS/DOGS/SMALL_ANIMALS/BIRDS/OTHER',
    `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`institution_id`) REFERENCES `user` (`id`),
    FOREIGN KEY (`recommended_shelter_id`) REFERENCES `shelter` (`id`),
    KEY `idx_status` (`status`),
    KEY `idx_breed` (`breed`),
    KEY `idx_category_type` (`category_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='宠物表（含智能匹配字段）';

-- 领养申请表
CREATE TABLE `adoption_application` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `applicant_id` bigint(20) NOT NULL COMMENT '申请人user_id',
    `pet_id` bigint(20) NOT NULL COMMENT '宠物id',
    `apply_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
    `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0待审核，1通过，2拒绝，3待补充，4待签署，5已完成',
    `review_comment` varchar(500) COMMENT '审核意见',
    `review_time` datetime COMMENT '审核时间',
    `family_members` varchar(500) COMMENT '家庭成员意见',
    `living_conditions` varchar(500) COMMENT '居住条件',
    `work_status` varchar(200) COMMENT '工作状况',
    `experience` text COMMENT '养宠经验',
    PRIMARY KEY (`id`),
    UNIQUE KEY `unique_application` (`applicant_id`, `pet_id`, `status`), -- 防止重复申请
    FOREIGN KEY (`applicant_id`) REFERENCES `user` (`id`),
    FOREIGN KEY (`pet_id`) REFERENCES `pet` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='领养申请表';

-- 领养协议表
CREATE TABLE `adoption_contract` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `application_id` bigint(20) NOT NULL COMMENT '关联申请id',
    `content` text NOT NULL COMMENT '协议内容',
    `sign_date` datetime COMMENT '签署时间',
    `sign_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0待签署，1已签署',
    `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`application_id`) REFERENCES `adoption_application` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='领养协议表';

-- 回访记录表
CREATE TABLE `follow_up_record` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `adoption_id` bigint(20) NOT NULL COMMENT '关联申请id',
    `follow_up_date` datetime NOT NULL COMMENT '回访日期',
    `follower_id` bigint(20) NOT NULL COMMENT '回访人user_id',
    `content` text COMMENT '回访内容',
    `pet_condition` varchar(500) NOT NULL COMMENT '宠物现状',
    `images` varchar(500) COMMENT '照片URL，多个用逗号分隔',
    `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`adoption_id`) REFERENCES `adoption_application` (`id`),
    FOREIGN KEY (`follower_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='回访记录表';

-- 任务表
CREATE TABLE `task` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `type` tinyint(1) NOT NULL COMMENT '0救助，1回访，2运输，3其他',
    `description` varchar(500) NOT NULL COMMENT '任务描述',
    `publisher_id` bigint(20) NOT NULL COMMENT '发布人user_id',
    `assignee_id` bigint(20) COMMENT '认领人user_id（志愿者）',
    `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0待认领，1进行中，2已完成，3取消',
    `deadline` datetime COMMENT '截止时间',
    `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`publisher_id`) REFERENCES `user` (`id`),
    FOREIGN KEY (`assignee_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务表';

-- 健康档案表
CREATE TABLE `health_record` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `pet_id` bigint(20) NOT NULL COMMENT '宠物id',
    `hospital_id` bigint(20) NOT NULL COMMENT '医院user_id',
    `record_type` tinyint(1) NOT NULL COMMENT '0疫苗，1体检，2治疗',
    `record_date` datetime NOT NULL COMMENT '记录时间',
    `details` text NOT NULL COMMENT '详情',
    `attachment` varchar(255) COMMENT '附件URL',
    `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`pet_id`) REFERENCES `pet` (`id`),
    FOREIGN KEY (`hospital_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='健康档案表';

-- 收藏表
CREATE TABLE `favorite` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `user_id` bigint(20) NOT NULL COMMENT '领养人user_id',
    `pet_id` bigint(20) NOT NULL COMMENT '宠物id',
    `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `unique_favorite` (`user_id`, `pet_id`), -- 确保同一用户不重复收藏
    FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    FOREIGN KEY (`pet_id`) REFERENCES `pet` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收藏表';

-- 系统日志表
CREATE TABLE `system_log` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `user_id` bigint(20) COMMENT '操作用户ID',
    `operation` varchar(100) NOT NULL COMMENT '操作类型',
    `content` text COMMENT '操作内容',
    `ip_address` varchar(45) COMMENT 'IP地址',
    `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统日志表';