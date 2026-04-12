-- ============================================================
-- 多申请人对比功能测试数据
-- 为花花（pet_id=5，状态待领养）创建 3 个待审核申请
-- ============================================================

-- 1. 插入 3 个新领养人账号（密码统一 123456，对应 BCrypt：$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi）
INSERT INTO `user` (`name`, `phone`, `password`, `role`, `status`) VALUES
('张三', '13800000011', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'adopter', 1),
('李四', '13800000012', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'adopter', 1),
('王五', '13800000013', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'adopter', 1);

-- 2. 为这 3 个用户创建 adopter 扩展信息（user_id 分别为 10, 11, 12）
INSERT INTO `adopter` (`user_id`, `id_card`, `address`, `experience`) VALUES
(10, '310101199501010011', '上海市浦东新区陆家嘴环路1000号', '有3年养猫经验，家里有两只猫'),
(11, '310101199601020022', '上海市徐汇区漕溪北路88号', '养猫新手，但经常做功课，了解英短的饲养知识'),
(12, '310101199703030033', '上海市长宁区延安西路2000号', '独居，有充足时间陪伴宠物，已有多次短期寄养经验');

-- 3. 为花花（pet_id=5）插入 3 条待审核申请（status=0）
INSERT INTO `adoption_application` (`applicant_id`, `pet_id`, `apply_date`, `status`, `family_members`, `living_conditions`, `work_status`, `experience`, `reason`) VALUES
(10, 5, '2026-04-10 09:00:00', 0, '独居，无家庭负担', '自有住房，一室一厅，有阳台', '自由职业，时间灵活', '有3年养猫经验，家里有两只猫', '一直很喜欢英短，三花色很特别，希望能给它一个温暖的家'),
(11, 5, '2026-04-10 14:30:00', 0, '单身，和室友合租', '整租公寓，有独立卧室，室友同意', '全职工作，偶尔加班', '养猫新手，但经常做功课', '花花的外貌和性格描述完全符合我的理想宠物，希望能被选中'),
(12, 5, '2026-04-11 08:45:00', 0, '独居，无宠物过敏', '自有小户型，采光好，适合猫咪生活', '自由职业，时间充裕', '独居，有充足时间陪伴宠物', '我有多次寄养经验，现在想要长期领养，希望能给花花稳定的生活');

-- 4. 花花当前状态已改为待领养（status=0），符合测试条件
-- 注意：如果花花 status 不是 0，需先执行：
-- UPDATE `pet` SET `status` = 0 WHERE `id` = 5;
