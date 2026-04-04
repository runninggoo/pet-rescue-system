-- 宠物救助收养系统测试数据
-- 在执行完 schema.sql 后运行此脚本

-- 插入测试用户
INSERT INTO `user` (`name`, `phone`, `password`, `role`, `status`) VALUES
('系统管理员', '13800000000', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'admin', 1),
('测试领养人', '13900000001', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'adopter', 1),
('测试机构管理员', '13900000002', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'institution_admin', 1),
('测试志愿者', '13900000003', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'volunteer', 1),
('测试宠物医院', '13900000004', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'hospital', 1);

-- 插入救助机构
INSERT INTO `institution` (`name`, `address`, `phone`, `status`) VALUES
('上海宠物救助中心', '上海市静安区南京西路1266号', '021-52391266', 1),
('浦东流浪动物救助站', '上海市浦东新区张江路456号', '021-58594567', 1),
('徐汇宠物收容所', '上海市徐汇区漕溪北路789号', '021-54297890', 1);

-- 插入上海地区救助所（智能匹配功能测试数据）
INSERT INTO `shelter` (`name`, `address`, `lat`, `lon`, `max_capacity`, `current_capacity`, `region_code`, `phone`, `status`, `medical_level`, `description`, `entry_status`, `audit_status`) VALUES
('上海爱心宠物救助中心', '上海市浦东新区张江路456号', 31.2397, 121.4998, 50, 25, '310115', '021-58594567', 0, 2, '浦东新区专业救助中心，提供24小时救助服务', 1, 1),
('上海小动物保护协会', '上海市徐汇区漕溪北路789号', 31.1667, 121.4167, 40, 30, '310104', '021-54297890', 0, 3, '徐汇区综合救助机构，设有专业医疗团队', 1, 1),
('流浪宠物救助站', '上海市闵行区虹桥路2000号', 31.1167, 121.4000, 30, 15, '310112', '021-52412345', 0, 1, '闵行区基础救助站，主要收容流浪猫狗', 1, 1),
('宠物福利中心', '上海市静安区南京西路1266号', 31.2397, 121.4444, 60, 45, '310106', '021-52391266', 0, 3, '静安区高端救助中心，提供全方位医疗服务', 1, 1),
('爱心动物医院救助部', '上海市长宁区虹桥路1500号', 31.2167, 121.4000, 35, 20, '310105', '021-52456789', 0, 3, '长宁区综合救助机构，医疗设施完善', 1, 1);

-- 更新机构管理员的所属机构
UPDATE `institution_admin` SET `institution_id` = 1 WHERE `user_id` = 2;

-- 插入领养人扩展信息
INSERT INTO `adopter` (`user_id`, `id_card`, `address`, `experience`) VALUES
(3, '310101199001011234', '上海市静安区南京西路1266号', '有5年养狗经验，养过金毛和拉布拉多');

-- 插入志愿者扩展信息
INSERT INTO `volunteer` (`user_id`, `service_area`, `task_types`) VALUES
(4, '上海市浦东新区', '救助,回访,运输');

-- 插入宠物医院扩展信息
INSERT INTO `hospital` (`user_id`, `hospital_name`, `address`, `license`) VALUES
(5, '上海宠物医院', '上海市徐汇区漕溪北路789号', '沪宠医证字[2024]第001号');

-- 插入测试宠物
INSERT INTO `pet` (`name`, `breed`, `age`, `gender`, `weight`, `color`, `health_status`, `personality`, `rescue_date`, `rescue_location`, `image_url`, `status`, `institution_id`, `description`, `rescue_lon`, `rescue_lat`, `urgency_level`, `expected_adoption_time`, `category_type`) VALUES
('小黄', '金毛寻回犬', 24, 1, 25.5, '金黄色', '健康状况良好，已接种疫苗，已驱虫', '性格温顺，喜欢与人互动，适合家庭饲养', '2024-01-15', '上海市静安区南京西路', 'https://images.unsplash.com/photo-1633722715463-d30f4f325e24?w=600&q=80', 0, 2, '小黄是在南京西路救助的流浪金毛，现在已经完全康复，适合领养', 121.4444, 31.2397, 0, 1, 'DOGS'),
('小黑', '中华田园犬', 12, 2, 15.2, '黑色', '健康状况良好，已接种疫苗', '性格活泼，聪明机灵，适应能力强', '2024-02-20', '上海市浦东新区张江路', 'https://images.unsplash.com/photo-1587300003388-59208cc962cb?w=600&q=80', 0, 2, '小黑是浦东地区的流浪狗，被志愿者发现后救助，现在很健康', 121.4998, 31.2397, 1, 2, 'DOGS'),
('小白', '波斯猫', 18, 2, 4.5, '白色', '健康状况良好，已驱虫，定期体检', '性格安静，温顺可爱，喜欢被抚摸', '2024-03-10', '上海市徐汇区漕溪北路', 'https://images.unsplash.com/photo-1573865526739-10659fec78a5?w=600&q=80', 0, 2, '小白是走失的宠物猫，被好心人送到救助站，现在寻找新主人', 121.4167, 31.1667, 0, 1, 'CATS'),
('旺财', '拉布拉多犬', 36, 1, 28.0, '巧克力色', '健康状况良好，定期体检，无遗传疾病', '性格忠诚，友善，对儿童友好', '2023-12-05', '上海市黄浦区外滩', 'https://images.unsplash.com/photo-1561037404-61cd46aa615b?w=600&q=80', 0, 2, '旺财是纯种拉布拉多，因主人搬家被遗弃，现在寻找有爱心的新主人', 121.4900, 31.2397, 0, 3, 'DOGS'),
('花花', '英国短毛猫', 8, 2, 3.8, '三花色', '健康状况良好，已接种疫苗，已绝育', '性格独立，爱干净，适应能力强', '2024-04-01', '上海市长宁区虹桥路', 'https://images.unsplash.com/photo-1592194996308-7b43878e84a6?w=600&q=80', 0, 2, '花花是小区里的流浪猫，被志愿者救助，现在很健康可爱', 121.4000, 31.2167, 2, 1, 'CATS');

-- 插入领养申请
INSERT INTO `adoption_application` (`applicant_id`, `pet_id`, `apply_date`, `status`, `family_members`, `living_conditions`, `work_status`, `experience`) VALUES
(3, 1, '2024-04-15 10:30:00', 1, '全家同意，没有反对意见', '有独立住房，带小院子，适合养狗', '稳定工作，有充足时间照顾宠物', '有5年养狗经验，养过金毛和拉布拉多，了解宠物习性'),
(3, 2, '2024-04-16 14:20:00', 0, '家庭成员支持，有老人可以陪伴', '公寓住房，有宠物活动空间', '工作时间灵活，可以照顾宠物', '有养狗经验，对宠物很有爱心'),
(3, 3, '2024-04-17 09:15:00', 2, '家人支持，孩子很喜欢猫', '两室一厅，环境安静', '稳定工作，经济条件良好', '第一次养猫，但对养宠物很有热情');

-- 插入领养协议
INSERT INTO `adoption_contract` (`application_id`, `content`, `sign_date`, `sign_status`) VALUES
(1, '领养协议内容：1. 领养人承诺善待宠物，提供良好的生活条件；2. 定期带宠物体检和接种疫苗；3. 不得随意遗弃或转让宠物；4. 接受救助机构的回访；5. 如有特殊情况需联系救助机构。', '2024-04-18 15:00:00', 1);

-- 插入回访记录
INSERT INTO `follow_up_record` (`adoption_id`, `follow_up_date`, `follower_id`, `content`, `pet_condition`, `images`) VALUES
(1, '2024-05-18 14:00:00', 4, '领养后第一个月回访，宠物适应良好，家庭成员都很喜欢。小黄很健康，体重有所增加，看起来很幸福。', '健康状况良好，体重27kg，精神状态很好，与家庭成员关系融洽', 'https://example.com/followup1.jpg,https://example.com/followup2.jpg');

-- 插入任务
INSERT INTO `task` (`type`, `description`, `publisher_id`, `assignee_id`, `status`, `deadline`, `created_time`) VALUES
(0, '在浦东新区张江路附近发现一只受伤的流浪狗，需要紧急救助', 2, 4, 1, '2024-04-20 18:00:00', '2024-04-19 09:00:00'),
(1, '对领养人ID为3的领养申请进行上门回访，了解宠物适应情况', 2, 4, 1, '2024-04-25 17:00:00', '2024-04-20 10:00:00'),
(2, '将救助的宠物从浦东救助站运输到徐汇收容所', 2, NULL, 0, '2024-04-22 12:00:00', '2024-04-21 08:00:00');

-- 插入健康档案
INSERT INTO `health_record` (`pet_id`, `hospital_id`, `record_type`, `record_date`, `details`, `attachment`) VALUES
(1, 5, 0, '2024-01-20', '接种狂犬疫苗，剂量1ml，无不良反应', 'https://example.com/vaccine1.pdf'),
(1, 5, 1, '2024-03-15', '常规体检：体重25kg，体温38.5℃，心率正常，无异常发现', 'https://example.com/checkup1.pdf'),
(2, 5, 0, '2024-02-25', '接种狂犬疫苗和六联疫苗，无不良反应', 'https://example.com/vaccine2.pdf'),
(3, 5, 2, '2024-03-12', '治疗皮肤过敏，使用抗过敏药物，效果良好', 'https://example.com/treatment1.pdf');

-- 插入收藏
INSERT INTO `favorite` (`user_id`, `pet_id`, `created_time`) VALUES
(3, 4, '2024-04-14 16:30:00'),
(3, 5, '2024-04-15 09:45:00');

-- 插入系统日志
INSERT INTO `system_log` (`user_id`, `operation`, `content`, `ip_address`, `created_time`) VALUES
(1, 'LOGIN', '系统管理员登录系统', '192.168.1.100', '2024-04-18 09:00:00'),
(2, 'ADD_PET', '添加新宠物：小黄', '192.168.1.101', '2024-04-18 10:30:00'),
(3, 'APPLY_ADOPTION', '提交领养申请，宠物ID：1', '192.168.1.102', '2024-04-18 14:20:00');

-- 显示插入的数据统计
SELECT '用户表数据量：' as description, COUNT(*) as count FROM `user`
UNION ALL
SELECT '机构表数据量：', COUNT(*) FROM `institution`
UNION ALL
SELECT '宠物表数据量：', COUNT(*) FROM `pet`
UNION ALL
SELECT '领养申请表数据量：', COUNT(*) FROM `adoption_application`
UNION ALL
SELECT '任务表数据量：', COUNT(*) FROM `task`
UNION ALL
SELECT '健康档案表数据量：', COUNT(*) FROM `health_record`
UNION ALL
SELECT '回访记录表数据量：', COUNT(*) FROM `follow_up_record`;

-- 显示测试账号信息
SELECT '测试账号信息：' as title
UNION ALL
SELECT CONCAT('管理员账号：13800000000 / 密码：123456')
UNION ALL
SELECT CONCAT('领养人账号：13900000001 / 密码：123456')
UNION ALL
SELECT CONCAT('机构管理员：13900000002 / 密码：123456')
UNION ALL
SELECT CONCAT('志愿者账号：13900000003 / 密码：123456')
UNION ALL
SELECT CONCAT('宠物医院：13900000004 / 密码：123456');