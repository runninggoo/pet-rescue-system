-- ================================================================
-- 补充缺失健康档案测试数据（2026-04-11）
-- 为全部 13 只缺失健康档案的宠物补充记录
-- ================================================================

-- pet_id=12 小黑 哈士奇 DOGS - 2条记录
INSERT INTO health_record (pet_id, record_type, title, record_date, content, institution, veterinarian, cost, next_reminder_date, status, deleted) VALUES
(12, 'vaccine', '犬四联疫苗首免', '2025-11-15', '完成犬四联疫苗第一针注射，包括犬瘟热、犬细小病毒、犬副流感和犬腺病毒，无过敏反应。', '浦东动物救助中心', '李医生', 120.00, '2026-05-15', 0, 0),
(12, 'checkup', '常规健康体检', '2026-01-10', '体重28.5kg，体温38.6℃，心率正常，心肺功能良好。血液检查无异常，体内外驱虫已完成。', '浦东动物救助中心', '张医生', 180.00, NULL, 0, 0);

-- pet_id=15 猫咪（美国短毛猫）CATS - 2条记录
INSERT INTO health_record (pet_id, record_type, title, record_date, content, institution, veterinarian, cost, next_reminder_date, status, deleted) VALUES
(15, 'vaccine', '猫三联疫苗加强针', '2025-12-20', '完成猫三联疫苗第三针加强注射，免疫效果良好，无不良反应。抗体检测合格。', '静安区宠物服务中心', '王医生', 150.00, '2026-12-20', 0, 0),
(15, 'treatment', '真菌感染治疗', '2026-02-05', '发现局部真菌感染，外用喷剂治疗2周后痊愈，皮肤恢复健康，毛发重新生长。', '静安区宠物服务中心', '王医生', 200.00, NULL, 0, 0);

-- pet_id=16 哈士奇（？）DOGS - 2条记录
INSERT INTO health_record (pet_id, record_type, title, record_date, content, institution, veterinarian, cost, next_reminder_date, status, deleted) VALUES
(16, 'vaccine', '狂犬疫苗接种', '2026-01-08', '完成狂犬疫苗接种，编号20260108，有效期至2027年01月08日。', '闵行区流浪动物救助中心', '陈医生', 80.00, '2027-01-08', 0, 0),
(16, 'checkup', '入站健康检查', '2026-01-08', '入站常规检查，体重正常，营养状况良好，无传染病迹象，身体健康。', '闵行区流浪动物救助中心', '陈医生', 100.00, NULL, 0, 0);

-- pet_id=18 蓝猫 CATS - 2条记录
INSERT INTO health_record (pet_id, record_type, title, record_date, content, institution, veterinarian, cost, next_reminder_date, status, deleted) VALUES
(18, 'vaccine', '猫三联疫苗首免', '2025-10-12', '完成猫三联疫苗第一针注射，建立基础免疫屏障。注射后观察30分钟无异常。', '上海小动物保护协会', '赵医生', 130.00, '2025-11-12', 0, 0),
(18, 'dental', '口腔清洁护理', '2026-03-01', '进行专业口腔清洁，去除牙结石，轻度牙龈炎需后续观察。', '上海小动物保护协会', '赵医生', 250.00, NULL, 0, 0);

-- pet_id=19 边牧 DOGS - 2条记录
INSERT INTO health_record (pet_id, record_type, title, record_date, content, institution, veterinarian, cost, next_reminder_date, status, deleted) VALUES
(19, 'vaccine', '犬四联疫苗加强针', '2026-02-18', '完成犬四联疫苗加强针注射，免疫程序完善。抗体滴度检测达标。', '浦东动物救助中心', '周医生', 120.00, '2027-02-18', 0, 0),
(19, 'checkup', '运动能力评估', '2026-02-18', '边牧身体素质优秀，运动能力强，四肢关节无异常，适合高强度训练。体重4.2kg达标。', '浦东动物救助中心', '周医生', 150.00, NULL, 0, 0);

-- pet_id=20 橘猫 CATS - 2条记录
INSERT INTO health_record (pet_id, record_type, title, record_date, content, institution, veterinarian, cost, next_reminder_date, status, deleted) VALUES
(20, 'vaccine', '猫三联疫苗首免', '2026-03-05', '橘猫入站后接种猫三联第一针，接种后精神食欲正常，无过敏反应。', '黄浦区宠物之家', '吴医生', 130.00, '2026-04-05', 0, 0),
(20, 'checkup', '入站体检', '2026-03-05', '体重3.8kg，体型偏瘦但健康状况良好，无传染病，无寄生虫，完成体内驱虫。', '黄浦区宠物之家', '吴医生', 100.00, NULL, 0, 0);

-- pet_id=26 萨摩耶 DOGS - 2条记录
INSERT INTO health_record (pet_id, record_type, title, record_date, content, institution, veterinarian, cost, next_reminder_date, status, deleted) VALUES
(26, 'vaccine', '犬六联疫苗首免', '2025-09-20', '完成犬六联疫苗第一针，包含犬瘟热、细小、副流感、腺病毒、钩端螺旋体、冠状病毒。', '静安区宠物服务中心', '郑医生', 180.00, '2025-10-20', 0, 0),
(26, 'checkup', '关节及骨骼检查', '2026-01-25', '萨摩耶骨骼发育良好，关节无异常。白毛护理建议定期洗澡梳毛。', '静安区宠物服务中心', '郑医生', 160.00, NULL, 0, 0);

-- pet_id=28 荷兰猪 SMALL_ANIMALS - 2条记录
INSERT INTO health_record (pet_id, record_type, title, record_date, content, institution, veterinarian, cost, next_reminder_date, status, deleted) VALUES
(28, 'checkup', '小宠常规体检', '2026-02-10', '荷兰猪体重正常780g，牙齿咬合正常，呼吸平稳，活泼好动。维生素C补充建议。', '上海小动物保护协会', '钱医生', 80.00, NULL, 0, 0),
(28, 'treatment', '真菌皮肤病治疗', '2026-02-25', '局部真菌感染，使用外用抗真菌喷剂治疗，配合药浴，一周后明显好转。', '上海小动物保护协会', '钱医生', 120.00, NULL, 0, 0);

-- pet_id=29 仓鼠 SMALL_ANIMALS - 2条记录
INSERT INTO health_record (pet_id, record_type, title, record_date, content, institution, veterinarian, cost, next_reminder_date, status, deleted) VALUES
(29, 'checkup', '小宠入站检查', '2026-03-15', '仓鼠体重正常12g，精神活泼，颊囊无储粮腐败，笼养环境清洁建议。', '闵行区流浪动物救助中心', '孙医生', 60.00, NULL, 0, 0),
(29, 'other', '饲养环境评估', '2026-03-15', '建议使用垫料更换频率提高，滚轮尺寸合适，饮食搭配均衡。', '闵行区流浪动物救助中心', '孙医生', 40.00, NULL, 0, 0);

-- pet_id=30 龙猫 SMALL_ANIMALS - 2条记录
INSERT INTO health_record (pet_id, record_type, title, record_date, content, institution, veterinarian, cost, next_reminder_date, status, deleted) VALUES
(30, 'checkup', '啮齿类健康评估', '2026-01-20', '龙猫体重380g，毛发浓密有光泽，体温正常。胃肠道功能正常，排便成型。', '浦东动物救助中心', '李医生', 90.00, NULL, 0, 0),
(30, 'vaccine', '体外驱虫', '2026-01-20', '完成外用驱虫滴剂使用，预防螨虫和跳蚤，建议每月定期驱虫。', '浦东动物救助中心', '李医生', 50.00, '2026-02-20', 0, 0);

-- pet_id=31 兔子 SMALL_ANIMALS - 2条记录
INSERT INTO health_record (pet_id, record_type, title, record_date, content, institution, veterinarian, cost, next_reminder_date, status, deleted) VALUES
(31, 'vaccine', '兔瘟疫苗接种', '2026-02-08', '完成兔瘟疫苗接种，有效期1年。接种后无不良反应，精神食欲正常。', '黄浦区宠物之家', '吴医生', 60.00, '2027-02-08', 0, 0),
(31, 'checkup', '消化系统检查', '2026-02-08', '兔子臼齿正常，无牙齿过长问题。盲肠消化功能良好，粪便形态正常。建议提摩西草无限量供应。', '黄浦区宠物之家', '吴医生', 100.00, NULL, 0, 0);

-- pet_id=32 金翅鹦鹉 SMALL_ANIMALS - 2条记录
INSERT INTO health_record (pet_id, record_type, title, record_date, content, institution, veterinarian, cost, next_reminder_date, status, deleted) VALUES
(32, 'checkup', '鸟类健康检查', '2026-03-01', '鹦鹉羽毛色泽鲜艳，喙爪正常，飞行能力良好。体重42g正常。无呼吸道症状。', '上海小动物保护协会', '赵医生', 70.00, NULL, 0, 0),
(32, 'other', '营养评估建议', '2026-03-01', '饮食结构良好，建议多样化水果蔬菜补充维生素。笼内设置攀爬玩具促进活动。', '上海小动物保护协会', '赵医生', 30.00, NULL, 0, 0);

-- pet_id=33 英短蓝猫 CATS - 2条记录
INSERT INTO health_record (pet_id, record_type, title, record_date, content, institution, veterinarian, cost, next_reminder_date, status, deleted) VALUES
(33, 'vaccine', '猫三联疫苗加强针', '2026-04-01', '完成英短蓝猫猫三联疫苗加强针，免疫程序完成。毛发浓密蓝色，品相优良。', '静安区宠物服务中心', '王医生', 150.00, '2027-04-01', 0, 0),
(33, 'checkup', '品种猫遗传病筛查', '2026-04-01', '心脏听诊无杂音，髋关节检查正常。HCM（肥厚性心肌病）和PKD（多囊肾病）筛查均未发现异常。', '静安区宠物服务中心', '王医生', 300.00, NULL, 0, 0);

-- 验证插入结果
SELECT p.id, p.name, p.breed, COUNT(hr.id) AS hr_count
FROM pet p
LEFT JOIN health_record hr ON p.id = hr.pet_id AND IFNULL(hr.deleted,0)=0
WHERE p.deleted=0
GROUP BY p.id ORDER BY p.id;

SELECT '健康档案总数:' AS info, COUNT(*) AS total FROM health_record WHERE IFNULL(deleted,0)=0;
