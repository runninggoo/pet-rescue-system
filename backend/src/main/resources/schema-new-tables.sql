-- ================================================
-- 宠物救助收养系统 - 新增数据表SQL
-- 创建时间: 2026-04-01
-- 包含: health_record表, volunteer_task表
-- ================================================

-- ================================================
-- 1. 健康档案表 (health_record)
-- ================================================
CREATE TABLE IF NOT EXISTS `health_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `deleted` INT DEFAULT 0 COMMENT '逻辑删除标志：0-未删除，1-已删除',
    `pet_id` BIGINT NOT NULL COMMENT '关联宠物ID',
    `record_type` VARCHAR(50) NOT NULL COMMENT '记录类型：vaccine-疫苗，checkup-体检，treatment-治疗，surgery-手术，dental-牙齿护理，other-其他',
    `title` VARCHAR(200) NOT NULL COMMENT '记录标题',
    `record_date` DATE COMMENT '记录日期',
    `content` TEXT COMMENT '记录内容/详情',
    `institution` VARCHAR(200) COMMENT '执行机构（宠物医院名称）',
    `veterinarian` VARCHAR(100) COMMENT '执行人/兽医名称',
    `cost` DECIMAL(10,2) COMMENT '费用（元）',
    `next_reminder_date` DATE COMMENT '下次提醒日期（用于疫苗提醒等）',
    `remark` TEXT COMMENT '备注',
    `images` TEXT COMMENT '照片URL，多个用逗号分隔',
    `status` INT DEFAULT 0 COMMENT '状态：0-正常，1-已过期，2-已取消',
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX `idx_pet_id` (`pet_id`),
    INDEX `idx_record_type` (`record_type`),
    INDEX `idx_record_date` (`record_date`),
    INDEX `idx_next_reminder` (`next_reminder_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='健康档案表';

-- ================================================
-- 2. 志愿者任务表 (volunteer_task)
-- ================================================
CREATE TABLE IF NOT EXISTS `volunteer_task` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `deleted` INT DEFAULT 0 COMMENT '逻辑删除标志：0-未删除，1-已删除',
    `title` VARCHAR(200) NOT NULL COMMENT '任务标题',
    `task_type` VARCHAR(50) NOT NULL COMMENT '任务类型：rescue-救助任务，transport-运输任务，care-照护任务，event-活动支持，medical-医疗协助，foster-临时寄养，other-其他',
    `description` TEXT COMMENT '任务描述',
    `status` INT DEFAULT 0 COMMENT '任务状态：0-待领取，1-进行中，2-已完成，3-已取消',
    `priority` INT DEFAULT 0 COMMENT '紧急程度：0-普通，1-紧急，2-非常紧急',
    `pet_id` BIGINT COMMENT '关联宠物ID（可选）',
    `shelter_id` BIGINT COMMENT '关联救助站ID',
    `location` VARCHAR(500) COMMENT '任务地点',
    `expected_date` DATE COMMENT '期望完成日期',
    `completed_date` DATE COMMENT '实际完成日期',
    `publish_date` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
    `publisher_id` BIGINT COMMENT '任务发布人ID',
    `volunteer_id` BIGINT COMMENT '任务接收人/志愿者ID',
    `reward_points` INT DEFAULT 0 COMMENT '任务奖励（积分）',
    `required_people` INT DEFAULT 1 COMMENT '所需人数',
    `signed_people` INT DEFAULT 0 COMMENT '已报名人数',
    `contact_phone` VARCHAR(20) COMMENT '联系电话',
    `estimated_hours` DECIMAL(5,1) COMMENT '任务耗时（小时）',
    `result` TEXT COMMENT '任务结果/总结',
    `created_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX `idx_status` (`status`),
    INDEX `idx_task_type` (`task_type`),
    INDEX `idx_priority` (`priority`),
    INDEX `idx_shelter_id` (`shelter_id`),
    INDEX `idx_volunteer_id` (`volunteer_id`),
    INDEX `idx_publish_date` (`publish_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='志愿者任务表';

-- ================================================
-- 3. 测试数据
-- ================================================

-- 健康档案测试数据
INSERT INTO `health_record` (`pet_id`, `record_type`, `title`, `record_date`, `content`, `institution`, `veterinarian`, `cost`, `next_reminder_date`, `remark`, `status`, `created_time`, `updated_time`) VALUES
-- 小橘 (pet_id=1)
(1, 'vaccine', '狂犬疫苗接种', DATE_SUB(CURDATE(), INTERVAL 30 DAY), '接种进口狂犬疫苗，保护期1年', '上海宠物医院', '张医生', 80.00, DATE_ADD(CURDATE(), INTERVAL 335 DAY), '疫苗接种记录，请保存好疫苗本', 0, NOW(), NOW()),
(1, 'checkup', '常规健康体检', DATE_SUB(CURDATE(), INTERVAL 60 DAY), '体温正常、心率正常、粪便检查无寄生虫', '上海宠物医院', '李医生', 150.00, DATE_ADD(CURDATE(), INTERVAL 60 DAY), '建议每季度体检一次', 0, NOW(), NOW()),
(1, 'vaccine', '猫三联疫苗加强针', DATE_SUB(CURDATE(), INTERVAL 15 DAY), '接种猫三联疫苗第三针', '上海宠物医院', '张医生', 120.00, DATE_ADD(CURDATE(), INTERVAL 350 DAY), '完成基础免疫', 0, NOW(), NOW()),

-- 小白 (pet_id=2)
(2, 'vaccine', '猫三联疫苗接种', DATE_SUB(CURDATE(), INTERVAL 45 DAY), '接种进口猫三联疫苗第一针', '浦东宠物诊所', '王医生', 120.00, DATE_ADD(CURDATE(), INTERVAL 45 DAY), '需在6周后接种第二针', 0, NOW(), NOW()),
(2, 'checkup', '新猫到家体检', DATE_SUB(CURDATE(), INTERVAL 50 DAY), '体格检查、耳道检查、皮肤检查均正常', '浦东宠物诊所', '王医生', 100.00, NULL, '建议适应期后进行疫苗接种', 0, NOW(), NOW()),

-- 花花 (pet_id=3)
(3, 'treatment', '皮肤病治疗', DATE_SUB(CURDATE(), INTERVAL 20 DAY), '真菌感染，口服药物+药浴治疗', '上海宠物医院', '张医生', 300.00, DATE_ADD(CURDATE(), INTERVAL 10 DAY), '需继续用药，一周后复查', 0, NOW(), NOW()),
(3, 'vaccine', '狂犬疫苗接种', DATE_SUB(CURDATE(), INTERVAL 25 DAY), '接种狂犬疫苗', '上海宠物医院', '张医生', 80.00, DATE_ADD(CURDATE(), INTERVAL 340 DAY), '已完成免疫', 0, NOW(), NOW()),

-- 黑猫警长 (pet_id=4)
(4, 'vaccine', '猫三联疫苗接种', DATE_SUB(CURDATE(), INTERVAL 10 DAY), '接种猫三联疫苗第二针', '上海小动物协会', '陈医生', 120.00, DATE_ADD(CURDATE(), INTERVAL 50 DAY), '即将接种第三针', 0, NOW(), NOW()),
(4, 'dental', '牙齿清洁护理', DATE_SUB(CURDATE(), INTERVAL 90 DAY), '超声波洁牙，抛光处理', '上海宠物医院', '张医生', 350.00, DATE_ADD(CURDATE(), INTERVAL 275 DAY), '建议每半年洁牙一次', 0, NOW(), NOW()),

-- 旺财 (pet_id=6)
(6, 'vaccine', '狂犬疫苗接种', DATE_SUB(CURDATE(), INTERVAL 5 DAY), '接种国产狂犬疫苗', '杨浦宠物中心', '刘医生', 60.00, DATE_ADD(CURDATE(), INTERVAL 360 DAY), '疫苗接种完成', 0, NOW(), NOW()),
(6, 'vaccine', '犬五联疫苗接种', DATE_SUB(CURDATE(), INTERVAL 35 DAY), '接种犬五联疫苗第一针', '杨浦宠物中心', '刘医生', 150.00, DATE_ADD(CURDATE(), INTERVAL 25 DAY), '需继续接种加强针', 0, NOW(), NOW()),
(6, 'checkup', '绝育手术前体检', DATE_SUB(CURDATE(), INTERVAL 40 DAY), '血常规检查、生化检查均正常', '杨浦宠物中心', '刘医生', 200.00, NULL, '体检合格，可进行手术', 0, NOW(), NOW()),

-- 毛毛 (pet_id=7)
(7, 'vaccine', '犬八联疫苗接种', DATE_SUB(CURDATE(), INTERVAL 60 DAY), '接种犬八联疫苗加强针', '上海宠物医院', '周医生', 200.00, DATE_ADD(CURDATE(), INTERVAL 60 DAY), '免疫加强中', 0, NOW(), NOW()),
(7, 'checkup', '年度健康大体检', DATE_SUB(CURDATE(), INTERVAL 90 DAY), '全面体检：血常规、生化、心脏超声均正常', '上海宠物医院', '周医生', 500.00, DATE_ADD(CURDATE(), INTERVAL 275 DAY), '金毛易患髋关节疾病，关注运动情况', 0, NOW(), NOW()),

-- 豆豆 (pet_id=8)
(8, 'vaccine', '狂犬疫苗接种', DATE_SUB(CURDATE(), INTERVAL 180 DAY), '接种狂犬疫苗', '黄浦宠物诊所', '赵医生', 80.00, DATE_ADD(CURDATE(), INTERVAL 185 DAY), '即将到期，请及时续种', 1, NOW(), NOW()),
(8, 'dental', '牙齿护理', DATE_SUB(CURDATE(), INTERVAL 120 DAY), '牙结石清理，牙龈护理', '黄浦宠物诊所', '赵医生', 280.00, DATE_ADD(CURDATE(), INTERVAL 245 DAY), '小型犬需定期护理牙齿', 0, NOW(), NOW());

-- 志愿者任务测试数据
INSERT INTO `volunteer_task` (`title`, `task_type`, `description`, `status`, `priority`, `pet_id`, `shelter_id`, `location`, `expected_date`, `publish_date`, `publisher_id`, `volunteer_id`, `reward_points`, `required_people`, `signed_people`, `contact_phone`, `estimated_hours`, `created_time`, `updated_time`) VALUES
-- 待领取任务
('救助受伤流浪猫', 'rescue', '在浦东新区陆家嘴附近发现一只受伤的流浪猫，需要紧急救助并送往附近救助站', 0, 2, 1, 1, '上海市浦东新区陆家嘴环路', DATE_ADD(CURDATE(), INTERVAL 1 DAY), NOW(), 1, NULL, 50, 2, 0, '021-58001234', 3.0, NOW(), NOW()),

('紧急转运宠物物资', 'transport', '从闵行仓库转运100公斤宠物粮食到浦东救助中心，需要志愿者帮忙搬运和运输', 0, 1, NULL, 2, '上海市闵行区莘庄镇 → 上海市浦东新区', DATE_ADD(CURDATE(), INTERVAL 2 DAY), NOW(), 1, NULL, 30, 3, 1, '021-58002345', 5.0, NOW(), NOW()),

('周末猫咪照护活动', 'care', '组织志愿者在救助站照护猫咪，包括喂食、清理猫砂、陪伴互动', 0, 0, NULL, 1, '上海市浦东新区浦东大道1234号', DATE_ADD(CURDATE(), INTERVAL 5 DAY), NOW(), 1, NULL, 20, 5, 2, '021-58001234', 4.0, NOW(), NOW()),

('协助宠物疫苗接种', 'medical', '协助宠物医院进行疫苗接种工作，维护现场秩序', 0, 1, NULL, 4, '上海市徐汇区漕河泾开发区', DATE_ADD(CURDATE(), INTERVAL 3 DAY), NOW(), 1, NULL, 40, 2, 0, '021-64001234', 3.5, NOW(), NOW()),

('救助站开放日活动支持', 'event', '协助举办救助站开放日活动，引导参观者，维护现场秩序', 0, 0, NULL, 2, '上海市浦东新区张江高科技园区', DATE_ADD(CURDATE(), INTERVAL 7 DAY), NOW(), 1, NULL, 25, 8, 3, '021-58002345', 6.0, NOW(), NOW()),

('临时寄养紧急救助犬', 'foster', '紧急需要临时寄养一只刚救助的德国牧羊犬，为期2周', 0, 2, 6, 14, '上海市杨浦区五角场镇', DATE_ADD(CURDATE(), INTERVAL 1 DAY), NOW(), 1, NULL, 60, 1, 0, '021-65101234', 336.0, NOW(), NOW()),

-- 进行中任务
('受伤金毛术后照护', 'care', '毛毛刚完成体检，需要志愿者协助术后照护，包括喂药、换药', 1, 1, 7, 1, '上海市浦东新区浦东大道1234号', DATE_ADD(CURDATE(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), 1, 10, 30, 2, 1, '021-58001234', 2.0, NOW(), NOW()),

-- 已完成任务
('流浪猫救助行动', 'rescue', '成功救助一只受伤的橘猫，送至浦东救助中心', 2, 1, 1, 1, '上海市浦东新区陆家嘴', DATE_SUB(CURDATE(), INTERVAL 5 DAY), DATE_SUB(NOW(), INTERVAL 10 DAY), 1, 10, 50, 2, 2, '021-58001234', 3.0, DATE_SUB(NOW(), INTERVAL 5 DAY), NOW()),

('宠物粮食搬运志愿活动', 'transport', '搬运500公斤宠物粮食入库', 2, 0, NULL, 3, '上海市闵行区莘庄镇', DATE_SUB(CURDATE(), INTERVAL 3 DAY), DATE_SUB(NOW(), INTERVAL 8 DAY), 1, 10, 25, 4, 4, '021-64003456', 4.0, DATE_SUB(NOW(), INTERVAL 3 DAY), NOW()),

('领养日活动支持', 'event', '协助举办周末领养日活动', 2, 0, NULL, 2, '上海市浦东新区张江高科技园区', DATE_SUB(CURDATE(), INTERVAL 7 DAY), DATE_SUB(NOW(), INTERVAL 14 DAY), 1, 10, 20, 6, 6, '021-58002345', 5.0, DATE_SUB(NOW(), INTERVAL 7 DAY), NOW()),

-- 已取消任务
('紧急宠物转运', 'transport', '临时取消，因救助站暂时满员', 3, 1, NULL, 5, '上海市徐汇区', DATE_ADD(CURDATE(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 3 DAY), 1, NULL, 35, 2, 0, '021-64002345', 4.0, DATE_SUB(NOW(), INTERVAL 3 DAY), NOW());
