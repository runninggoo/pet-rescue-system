-- ================================================
-- 宠物救助收养系统 - 测试数据更新脚本
-- 更新时间: 2026-04-02
-- 更新内容:
--   1. 更新宠物图片为真实品种照片
--   2. 新增更多宠物数据（扩大演示规模）
--   3. 新增更多志愿者任务数据
--   4. 新增更多健康档案数据
-- ================================================

-- ================================================
-- 1. 更新现有宠物的图片（品种匹配的真实照片）
-- ================================================
UPDATE pet SET image_url = 'https://images.unsplash.com/photo-1552053831-71594a27632d?w=600&q=80' WHERE name = '小橘' AND breed = '中华田园猫';
UPDATE pet SET image_url = 'https://images.unsplash.com/photo-1573865526739-10659fec78a5?w=600&q=80' WHERE name = '小白' AND breed = '英国短毛猫';
UPDATE pet SET image_url = 'https://images.unsplash.com/photo-1513245543132-31f507417b26?w=600&q=80' WHERE name = '花花' AND breed = '三花猫';
UPDATE pet SET image_url = 'https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba?w=600&q=80' WHERE name = '黑猫警长' AND breed = '黑色田园猫';
UPDATE pet SET image_url = 'https://images.unsplash.com/photo-1495360010541-f48722b34f7d?w=600&q=80' WHERE name = '小狸' AND breed = '狸花猫';
UPDATE pet SET image_url = 'https://images.unsplash.com/photo-1587300003388-59208cc962cb?w=600&q=80' WHERE name = '旺财' AND breed = '中华田园犬';
UPDATE pet SET image_url = 'https://images.unsplash.com/photo-1633722715463-d30f4f325e24?w=600&q=80' WHERE name = '毛毛' AND breed = '金毛寻回犬';
UPDATE pet SET image_url = 'https://images.unsplash.com/photo-1616149702344-c48c0c07f756?w=600&q=80' WHERE name = '豆豆' AND breed = '泰迪犬';
UPDATE pet SET image_url = 'https://images.unsplash.com/photo-1561037404-61cd46aa615b?w=600&q=80' WHERE name = '小黑' AND breed = '拉布拉多';
UPDATE pet SET image_url = 'https://images.unsplash.com/photo-1605568427561-40dd23c2acea?w=600&q=80' WHERE name = '笨笨' AND breed = '哈士奇';

-- ================================================
-- 2. 新增更多宠物数据（扩大演示规模）
-- ================================================
INSERT INTO pet (name, breed, age, gender, weight, color, health_status, personality, rescue_date, rescue_location, image_url, status, institution_id, description, rescue_lon, rescue_lat, urgency_level, expected_adoption_time, created_time, updated_time, category_type) VALUES
-- 猫咪
('橘子', '中华田园猫', 10, 1, 4.2, '橘白', '健康状况良好，已绝育', '性格温顺亲人，喜欢被摸', DATE_SUB(NOW(), INTERVAL 6 DAY), '上海市徐汇区衡山路', 'https://images.unsplash.com/photo-1574158622682-e40e69881006?w=600&q=80', 0, 5, '橘白相间的小帅哥，非常亲人，已绝育，适合家庭领养', 121.4469, 31.2089, 0, 1, NOW(), NOW(), 'CATS'),
('雪球', '英国短毛猫', 18, 2, 4.8, '纯白', '健康状况良好', '性格安静，喜欢独处', DATE_SUB(NOW(), INTERVAL 14 DAY), '上海市静安区北京西路', 'https://images.unsplash.com/photo-1543852786-1cf6624b9987?w=600&q=80', 0, 8, '纯白英短，眼睛非常有神，非常安静乖巧', 121.4544, 31.2325, 0, 2, NOW(), NOW(), 'CATS'),
('团子', '美国短毛猫', 12, 2, 4.0, '虎斑', '健康，已驱虫', '活泼好动，喜欢玩逗猫棒', DATE_SUB(NOW(), INTERVAL 9 DAY), '上海市长宁区虹桥路', 'https://images.unsplash.com/photo-1592194996308-7b43878e84a6?w=600&q=80', 0, 11, '虎斑美短，非常活泼可爱，适合有小孩的家庭', 121.3929, 31.2018, 0, 1, NOW(), NOW(), 'CATS'),
('煤球', '黑色田园猫', 8, 1, 4.5, '纯黑色', '健康，已绝育', '性格高冷但会撒娇', DATE_SUB(NOW(), INTERVAL 4 DAY), '上海市浦东新区陆家嘴', 'https://images.unsplash.com/photo-1519052537078-e6302a4968cc?w=600&q=80', 0, 1, '纯黑田园猫，非常帅气，高冷但内心温柔', 121.5437, 31.2247, 1, 1, NOW(), NOW(), 'CATS'),
('年糕', '苏格兰折耳猫', 24, 2, 3.5, '奶油色', '健康状况良好', '乖巧安静，非常粘人', DATE_SUB(NOW(), INTERVAL 18 DAY), '上海市闵行区莘庄', 'https://images.unsplash.com/photo-1518791841217-8f162f1e1131?w=600&q=80', 0, 7, '奶油色折耳猫，性格非常乖巧，耳朵折起来很可爱', 121.3776, 31.1056, 0, 2, NOW(), NOW(), 'CATS'),
('汤圆', '布偶猫', 14, 2, 4.2, '海豹色', '健康状况良好', '温柔亲人，被称为猫中仙女', DATE_SUB(NOW(), INTERVAL 11 DAY), '上海市黄浦区南京东路', 'https://images.unsplash.com/photo-1543466835-00a7907e9de1?w=600&q=80', 0, 13, '海豹色布偶猫，毛发飘逸如仙女，非常温顺', 121.4832, 31.2317, 0, 2, NOW(), NOW(), 'CATS'),
-- 狗狗
('大黄', '金毛寻回犬', 48, 1, 30.0, '金黄色', '健康状况良好', '性格温顺，对人友好', DATE_SUB(NOW(), INTERVAL 25 DAY), '上海市宝山区顾村', 'https://images.unsplash.com/photo-1633722715463-d30f4f325e24?w=600&q=80', 0, 16, '纯种金毛，性格极其温顺，非常适合有小孩的家庭', 121.3542, 31.4028, 0, 3, NOW(), NOW(), 'DOGS'),
('小七', '拉布拉多', 36, 2, 26.0, '奶油色', '健康状况良好', '聪明活泼，学习能力极强', DATE_SUB(NOW(), INTERVAL 16 DAY), '上海市嘉定区安亭', 'https://images.unsplash.com/photo-1561037404-61cd46aa615b?w=600&q=80', 0, 19, '奶油色拉布拉多，非常聪明，曾是导盲犬候选', 121.1689, 31.3742, 0, 2, NOW(), NOW(), 'DOGS'),
('阿福', '柴犬', 30, 1, 10.5, '赤色', '健康状况良好', '性格倔强但非常可爱', DATE_SUB(NOW(), INTERVAL 8 DAY), '上海市杨浦区五角场', 'https://images.unsplash.com/photo-1587300003388-59208cc962cb?w=600&q=80', 0, 14, '赤色柴犬，表情包本包，非常治愈', 121.5189, 31.3031, 0, 1, NOW(), NOW(), 'DOGS'),
('小雪', '萨摩耶', 28, 2, 22.0, '白色', '健康状况良好', '微笑天使，非常活泼', DATE_SUB(NOW(), INTERVAL 20 DAY), '上海市虹口区四川北路', 'https://images.unsplash.com/photo-1605568427561-40dd23c2acea?w=600&q=80', 0, 15, '萨摩耶，毛发雪白，会微笑的天使狗狗', 121.4923, 31.2642, 1, 2, NOW(), NOW(), 'DOGS'),
('哈利', '比熊犬', 20, 1, 6.5, '白色', '健康，已做美容', '活泼可爱，像棉花糖一样', DATE_SUB(NOW(), INTERVAL 5 DAY), '上海市普陀区曹杨路', 'https://images.unsplash.com/photo-1616149702344-c48c0c07f756?w=600&q=80', 0, 12, '白色比熊，像一团棉花糖，非常适合公寓饲养', 121.3912, 31.2428, 0, 1, NOW(), NOW(), 'DOGS'),
('贝贝', '柯基', 36, 2, 12.0, '黄白', '健康状况良好', '腿短屁股圆，非常可爱', DATE_SUB(NOW(), INTERVAL 13 DAY), '上海市松江区大学城', 'https://images.unsplash.com/photo-1587300003388-59208cc962cb?w=600&q=80', 0, 18, '黄白柯基，小短腿大屁股，走路很萌', 121.2289, 31.0317, 0, 2, NOW(), NOW(), 'DOGS'),
-- 小动物
('小灰', '荷兰猪', 6, 1, 0.9, '灰色', '健康状况良好', '性格温顺，适合互动', DATE_SUB(NOW(), INTERVAL 7 DAY), '上海市青浦区徐泾', 'https://images.unsplash.com/photo-1425082661705-1834bfd09dca?w=600&q=80', 0, 20, '荷兰猪，性格温顺，喂食时会发出可爱的声音', 121.3089, 31.1592, 0, 1, NOW(), NOW(), 'SMALL_ANIMALS'),
('毛球', '仓鼠', 3, 2, 0.15, '金黄色', '健康状况良好', '活泼好动，跑轮子冠军', DATE_SUB(NOW(), INTERVAL 2 DAY), '上海市奉贤区南桥', 'https://images.unsplash.com/photo-1425082661705-1834bfd09dca?w=600&q=80', 0, 21, '金丝熊仓鼠，圆滚滚的非常可爱', 121.4589, 30.9189, 0, 1, NOW(), NOW(), 'SMALL_ANIMALS');

-- ================================================
-- 3. 新增更多志愿者任务数据
-- ================================================
INSERT INTO volunteer_task (title, task_type, priority, description, location, shelter_id, publisher_id, expected_date, contact_phone, reward_points, estimated_hours, required_people, status, created_time, updated_time) VALUES
-- 救助任务
('紧急救助：浦东新区受伤流浪狗', 'rescue', 2, '在浦东新区张江地铁站附近发现一只后腿受伤的流浪狗，急需志愿者前往救助并送医。请有救助经验的志愿者积极参与！', '上海市浦东新区张江地铁站', 1, 1, DATE_ADD(NOW(), INTERVAL 1 DAY), '021-58001234', 50, 3, 2, 0, NOW(), NOW()),
('杨浦区受伤猫咪紧急救助', 'rescue', 2, '杨浦区五角场小区内发现一只后腿受伤的猫咪，无法行走，需要紧急救助。', '上海市杨浦区五角场镇', 14, 1, DATE_ADD(NOW(), INTERVAL 1 DAY), '021-65101234', 40, 2, 1, 0, NOW(), NOW()),
('闵行区走失宠物搜寻', 'rescue', 1, '闵行区莘庄镇走失一只柯基犬，黄色，公犬，佩戴蓝色项圈。请志愿者协助搜寻。', '上海市闵行区莘庄镇', 7, 1, DATE_ADD(NOW(), INTERVAL 3 DAY), '021-64003456', 30, 4, 3, 0, NOW(), NOW()),

-- 运输任务
('宠物跨区转运协助', 'transport', 1, '需要将3只救助猫咪从浦东救助中心转运至徐汇区宠物救助站，需要志愿者协助装载和运输。', '上海市浦东新区浦东大道1234号 → 上海市徐汇区漕河泾开发区', 1, 1, DATE_ADD(NOW(), INTERVAL 2 DAY), '021-58001234', 35, 3, 2, 0, NOW(), NOW()),
('救助物资运送', 'transport', 0, '将一批救助物资（狗粮、猫粮、药品）从静安区仓库运送至各救助站点。', '上海市静安区北京西路 → 各救助站', 8, 1, DATE_ADD(NOW(), INTERVAL 4 DAY), '021-63101234', 25, 2, 3, 0, NOW(), NOW()),
('紧急宠物送医', 'transport', 2, '青浦区救助站一只宠物病情加重，需要紧急送至上海市中心宠物医院救治。', '上海市青浦区徐泾镇 → 上海市徐汇区', 20, 1, DATE_ADD(NOW(), INTERVAL 0 DAY), '021-69201234', 60, 2, 1, 0, NOW(), NOW()),

-- 照护任务
('救助站日常清洁消毒', 'care', 0, '浦东救助中心需要志愿者协助进行笼舍清洁、消毒，以及宠物喂食照料工作。', '上海市浦东新区浦东大道1234号', 1, 1, DATE_ADD(NOW(), INTERVAL 2 DAY), '021-58001234', 20, 4, 5, 0, NOW(), NOW()),
('猫咪社会化训练', 'care', 0, '对救助站内的几只流浪猫进行社会化训练，帮助它们适应人类接触，提高领养率。', '上海市徐汇区漕河泾开发区', 4, 1, DATE_ADD(NOW(), INTERVAL 5 DAY), '021-64001234', 15, 3, 3, 0, NOW(), NOW()),
('幼犬基础服从训练', 'care', 1, '对救助站内的几只幼犬进行基础服从训练（坐下、握手、不扑人等），便于后续领养。', '上海市闵行区莘庄镇', 7, 1, DATE_ADD(NOW(), INTERVAL 3 DAY), '021-64003456', 25, 3, 2, 0, NOW(), NOW()),
('隔离期宠物护理', 'care', 1, '新救助宠物需要7天隔离观察期，需要志愿者协助每日喂食、清理和记录健康状况。', '上海市长宁区虹桥路2285号', 11, 1, DATE_ADD(NOW(), INTERVAL 1 DAY), '021-62101234', 30, 2, 2, 0, NOW(), NOW()),

-- 医疗协助
('宠物疫苗接种协助', 'medical', 0, '本周六救助站集中疫苗接种日，需要志愿者协助医生进行疫苗接种和记录工作。', '上海市静安区共和新路', 9, 1, DATE_ADD(NOW(), INTERVAL 5 DAY), '021-63102345', 30, 4, 4, 0, NOW(), NOW()),
('宠物体检日协助', 'medical', 1, '与宠物医院合作进行救助宠物全面体检，需要志愿者协助引导和记录。', '上海市浦东新区张江高科技园区', 2, 1, DATE_ADD(NOW(), INTERVAL 3 DAY), '021-58002345', 25, 3, 3, 0, NOW(), NOW()),

-- 临时寄养
('春节宠物临时寄养家庭招募', 'foster', 1, '春节期间（2周），需要招募5个临时寄养家庭照顾救助站内部分宠物。', '上海市各区', NULL, 1, DATE_ADD(NOW(), INTERVAL 7 DAY), '021-58001234', 40, 0, 5, 0, NOW(), NOW()),
('术后宠物家庭寄养', 'foster', 1, '一只刚完成腿部手术的田园犬需要寄养2周，方便术后护理和恢复。', '上海市徐汇区', 5, 1, DATE_ADD(NOW(), INTERVAL 2 DAY), '021-64002345', 50, 0, 1, 0, NOW(), NOW()),

-- 活动支持
('周末领养日活动支持', 'event', 0, '本月第三个周六在静安区举办大型领养日活动，需要志愿者协助布展、接待和现场解说。', '上海市静安区北京西路1068号', 8, 1, DATE_ADD(NOW(), INTERVAL 10 DAY), '021-63101234', 20, 5, 8, 0, NOW(), NOW()),
('校园流浪动物宣传', 'event', 0, '在上海大学校园内进行流浪动物保护宣传，发放宣传资料和救助热线卡片。', '上海市宝山区上海大学', NULL, 1, DATE_ADD(NOW(), INTERVAL 8 DAY), '021-56101234', 15, 3, 6, 0, NOW(), NOW());

-- ================================================
-- 4. 新增更多健康档案数据
-- ================================================
INSERT INTO health_record (pet_id, record_type, title, record_date, institution, veterinarian, cost, content, next_reminder_date, status, created_time, updated_time) VALUES
-- 小橘（pet_id=1）的健康档案
(1, 'vaccine', '猫三联疫苗加强针', DATE_SUB(NOW(), INTERVAL 30 DAY), '上海宠物医院', '张医生', 150.00, '接种猫三联疫苗第五次加强针，无不良反应，免疫效果良好', DATE_ADD(NOW(), INTERVAL 335 DAY), 0, NOW(), NOW()),
(1, 'checkup', '年度健康体检', DATE_SUB(NOW(), INTERVAL 60 DAY), '上海宠物医院', '李医生', 200.00, '年度体检结果：体重4.2kg，体温正常，心肺功能良好，无寄生虫，建议继续观察', NULL, 0, NOW(), NOW()),

-- 小白（pet_id=2）的健康档案
(2, 'vaccine', '猫三联疫苗接种', DATE_SUB(NOW(), INTERVAL 15 DAY), '上海宠物医院', '王医生', 120.00, '首次接种猫三联疫苗第一针，接种后观察30分钟无异常', DATE_ADD(NOW(), INTERVAL 15 DAY), 0, NOW(), NOW()),
(2, 'checkup', '绝育术后复查', DATE_SUB(NOW(), INTERVAL 45 DAY), '上海宠物医院', '张医生', 80.00, '绝育手术后第7天复查，切口愈合良好，精神食欲正常', NULL, 0, NOW(), NOW()),
(2, 'dental', '口腔清洁护理', DATE_SUB(NOW(), INTERVAL 90 DAY), '上海宠物牙科中心', '陈医生', 300.00, '进行超声波洁牙，清除牙结石，抛光处理，口腔健康状况良好', DATE_ADD(NOW(), INTERVAL 275 DAY), 0, NOW(), NOW()),

-- 花花（pet_id=3）的健康档案
(3, 'vaccine', '狂犬疫苗接种', DATE_SUB(NOW(), INTERVAL 7 DAY), '上海宠物医院', '李医生', 80.00, '接种狂犬疫苗，无过敏反应，抗体检测合格', DATE_ADD(NOW(), INTERVAL 358 DAY), 0, NOW(), NOW()),
(3, 'treatment', '皮肤病治疗', DATE_SUB(NOW(), INTERVAL 20 DAY), '上海宠物医院', '王医生', 350.00, '确诊为真菌性皮肤病，使用抗真菌药浴和外用药治疗，疗程2周', DATE_ADD(NOW(), INTERVAL 8 DAY), 0, NOW(), NOW()),

-- 毛毛（pet_id=8/金毛）的健康档案
(8, 'vaccine', '狂犬疫苗及联苗接种', DATE_SUB(NOW(), INTERVAL 10 DAY), '上海宠物医院', '张医生', 180.00, '接种狂犬疫苗和钩端螺旋体疫苗，接种后无不良反应', DATE_ADD(NOW(), INTERVAL 355 DAY), 0, NOW(), NOW()),
(8, 'checkup', '运动功能评估', DATE_SUB(NOW(), INTERVAL 30 DAY), '上海宠物骨科中心', '骨科刘医生', 280.00, '金毛后腿髋关节X光检查，轻度发育不良，建议补充软骨素，控制体重', DATE_ADD(NOW(), INTERVAL 60 DAY), 0, NOW(), NOW()),

-- 新增宠物（pet_id=11-22）的健康档案
-- 橘子猫（pet_id=11）
(11, 'vaccine', '猫三联疫苗第三针', DATE_SUB(NOW(), INTERVAL 5 DAY), '徐汇宠物诊所', '林医生', 120.00, '完成猫三联疫苗第三针，免疫程序完成，无不良反应', DATE_ADD(NOW(), INTERVAL 365 DAY), 0, NOW(), NOW()),

-- 雪球猫（pet_id=12）
(12, 'vaccine', '年度疫苗加强', DATE_SUB(NOW(), INTERVAL 20 DAY), '静安宠物医院', '赵医生', 150.00, '英短年度疫苗加强接种，抗体水平检测正常', DATE_ADD(NOW(), INTERVAL 345 DAY), 0, NOW(), NOW()),
(12, 'checkup', '心脏彩超检查', DATE_SUB(NOW(), INTERVAL 40 DAY), '上海宠物心脏中心', '心内科周医生', 450.00, '英短常见心脏病筛查，心脏功能正常，无杂音，建议定期复查', DATE_ADD(NOW(), INTERVAL 325 DAY), 0, NOW(), NOW()),

-- 大黄狗（pet_id=16/金毛）
(16, 'vaccine', '狂犬疫苗接种', DATE_SUB(NOW(), INTERVAL 8 DAY), '宝山宠物诊所', '马医生', 80.00, '接种狂犬疫苗，芯片扫描登记，无不良反应', DATE_ADD(NOW(), INTERVAL 357 DAY), 0, NOW(), NOW()),
(16, 'treatment', '皮肤过敏治疗', DATE_SUB(NOW(), INTERVAL 25 DAY), '上海宠物医院', '张医生', 380.00, '食物过敏导致皮肤瘙痒，口服抗过敏药配合药浴，一周后明显好转', DATE_ADD(NOW(), INTERVAL 5 DAY), 0, NOW(), NOW()),

-- 小七狗（pet_id=17/拉布拉多）
(17, 'vaccine', '犬类联苗接种', DATE_SUB(NOW(), INTERVAL 3 DAY), '嘉定宠物医院', '孙医生', 160.00, '接种犬类八联疫苗，接种后观察正常', DATE_ADD(NOW(), INTERVAL 362 DAY), 0, NOW(), NOW()),
(17, 'checkup', '眼睛检查', DATE_SUB(NOW(), INTERVAL 50 DAY), '上海宠物眼科中心', '眼科李医生', 220.00, '拉布拉多常见白内障筛查，双眼清澈，无遗传性眼病征兆', DATE_ADD(NOW(), INTERVAL 315 DAY), 0, NOW(), NOW()),

-- 阿福狗（pet_id=18/柴犬）
(18, 'vaccine', '狂犬疫苗接种', DATE_SUB(NOW(), INTERVAL 12 DAY), '杨浦宠物医院', '陈医生', 80.00, '接种狂犬疫苗，办理犬类免疫证明', DATE_ADD(NOW(), INTERVAL 353 DAY), 0, NOW(), NOW()),
(18, 'checkup', '年度体检', DATE_SUB(NOW(), INTERVAL 60 DAY), '杨浦宠物医院', '陈医生', 260.00, '柴犬年度全面体检，血常规正常，肝肾功能良好，体重标准', NULL, 0, NOW(), NOW());

-- ================================================
-- 5. 确认更新结果
-- ================================================
SELECT '=== 数据更新完成 ===' AS message;
SELECT CONCAT('宠物总数: ', COUNT(*)) AS stat FROM pet WHERE deleted = 0;
SELECT CONCAT('志愿者任务总数: ', COUNT(*)) AS stat FROM volunteer_task WHERE deleted = 0;
SELECT CONCAT('健康档案总数: ', COUNT(*)) AS stat FROM health_record WHERE deleted = 0;
