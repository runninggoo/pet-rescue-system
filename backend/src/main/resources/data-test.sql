-- ================================================
-- 宠物救助收养系统 - 综合测试数据（全量扩充版）
-- 适用版本: v1.0.6+
-- 说明: 密码统一为 123456（BCrypt加密）
-- 执行顺序: 在 schema.sql 之后运行此脚本
-- 注意: operation_log 和 pet_category 表不存在时会自动创建
-- ================================================

-- ================================================
-- 0. 补充建表语句（仅在表不存在时创建，防止重复执行报错）
-- ================================================
CREATE TABLE IF NOT EXISTS `operation_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT COMMENT '用户ID',
    `username` VARCHAR(100) COMMENT '用户名',
    `operation` VARCHAR(100) NOT NULL COMMENT '操作类型：LOGIN/LOGOUT/APPLICATION/AUDIT/PET_MANAGE/PET_ADD/PET_STATUS/RECORD_ADD/TASK_CLAIM/TASK_COMPLETE...',
    `detail` TEXT COMMENT '操作详情JSON',
    `ip_address` VARCHAR(45) COMMENT 'IP地址',
    `user_role` VARCHAR(50) COMMENT '用户角色',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_operation` (`operation`),
    INDEX `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

CREATE TABLE IF NOT EXISTS `pet_category` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name` VARCHAR(100) NOT NULL COMMENT '分类名称',
    `parent_id` BIGINT DEFAULT NULL COMMENT '父级ID，NULL表示一级分类',
    `type` VARCHAR(50) DEFAULT NULL COMMENT '大类：CATS/DOGS/SMALL_ANIMALS/BIRDS/OTHER',
    `sort` INT DEFAULT 0 COMMENT '排序序号',
    `is_active` INT DEFAULT 1 COMMENT '是否启用：0-禁用 1-启用',
    PRIMARY KEY (`id`),
    INDEX `idx_parent_id` (`parent_id`),
    INDEX `idx_type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='宠物分类表';

-- ================================================
-- 宠物分类初始数据
-- ================================================
INSERT INTO pet_category (id, name, parent_id, type, sort, is_active) VALUES
-- 一级分类
(1,  '猫',          NULL, 'CATS',            1, 1),
(2,  '狗',          NULL, 'DOGS',            2, 1),
(3,  '小动物',      NULL, 'SMALL_ANIMALS',   3, 1),
(4,  '鸟类',        NULL, 'BIRDS',           4, 1),
(5,  '其他',        NULL, 'OTHER',           5, 1),
-- 猫的品种分类
(10, '中华田园猫',  1,    'CATS',            10, 1),
(11, '英国短毛猫',  1,    'CATS',            11, 1),
(12, '美国短毛猫',  1,    'CATS',            12, 1),
(13, '波斯猫',      1,    'CATS',            13, 1),
(14, '暹罗猫',      1,    'CATS',            14, 1),
(15, '布偶猫',      1,    'CATS',            15, 1),
(16, '狸花猫',      1,    'CATS',            16, 1),
(17, '无毛猫',      1,    'CATS',            17, 1),
-- 狗的品种分类
(20, '金毛寻回犬',  2,    'DOGS',            20, 1),
(21, '拉布拉多',    2,    'DOGS',            21, 1),
(22, '泰迪犬',      2,    'DOGS',            22, 1),
(23, '哈士奇',      2,    'DOGS',            23, 1),
(24, '柯基',        2,    'DOGS',            24, 1),
(25, '柴犬',        2,    'DOGS',            25, 1),
(26, '萨摩耶',      2,    'DOGS',            26, 1),
(27, '中华田园犬',  2,    'DOGS',            27, 1),
(28, '边境牧羊犬',  2,    'DOGS',            28, 1),
(29, '比熊犬',      2,    'DOGS',            29, 1),
-- 小动物分类
(30, '兔子',        3,    'SMALL_ANIMALS',  30, 1),
(31, '仓鼠',        3,    'SMALL_ANIMALS',  31, 1),
(32, '荷兰猪',      3,    'SMALL_ANIMALS',  32, 1)
ON DUPLICATE KEY UPDATE name = VALUES(name);

-- ================================================
-- 1. 用户数据（34人）
-- ================================================
-- 管理员（2人）
INSERT INTO user (id, name, phone, password, role, status, created_time, updated_time) VALUES
(1, '系统管理员', '13800000000', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'admin', 1, NOW(), NOW()),
(2, '超级管理员', '13800000001', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'admin', 1, NOW(), NOW());

-- 领养人（13人，id=3~15）
INSERT INTO user (id, name, phone, password, role, status, created_time, updated_time) VALUES
(3,  '陈建国', '13900000001', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'adopter', 1, NOW(), NOW()),
(4,  '林晓敏', '13900000002', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'adopter', 1, NOW(), NOW()),
(5,  '王海峰', '13900000003', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'adopter', 1, NOW(), NOW()),
(6,  '张雨晴', '13900000004', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'adopter', 1, NOW(), NOW()),
(7,  '刘志强', '13900000005', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'adopter', 1, NOW(), NOW()),
(8,  '赵雅琴', '13900000006', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'adopter', 1, NOW(), NOW()),
(9,  '孙浩然', '13900000007', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'adopter', 1, NOW(), NOW()),
(10, '周文静', '13900000008', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'adopter', 1, NOW(), NOW()),
(11, '吴亦凡', '13900000009', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'adopter', 1, NOW(), NOW()),
(12, '郑美玲', '13900000010', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'adopter', 1, NOW(), NOW()),
(13, '黄志伟', '13900000011', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'adopter', 1, NOW(), NOW()),
(14, '李思琪', '13900000012', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'adopter', 1, NOW(), NOW()),
(15, '杨明远', '13900000013', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'adopter', 1, NOW(), NOW());

-- 机构管理员（10人，id=16~25，每个救助站1个）
INSERT INTO user (id, name, phone, password, role, status, created_time, updated_time) VALUES
(16, '浦东救助中心管理员', '13700000001', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'institution_admin', 1, NOW(), NOW()),
(17, '徐汇救助站管理员', '13700000002', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'institution_admin', 1, NOW(), NOW()),
(18, '闵行救助站管理员', '13700000003', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'institution_admin', 1, NOW(), NOW()),
(19, '静安救助站管理员', '13700000004', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'institution_admin', 1, NOW(), NOW()),
(20, '长宁救助站管理员', '13700000005', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'institution_admin', 1, NOW(), NOW()),
(21, '黄浦救助站管理员', '13700000006', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'institution_admin', 1, NOW(), NOW()),
(22, '杨浦救助站管理员', '13700000007', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'institution_admin', 1, NOW(), NOW()),
(23, '宝山救助站管理员', '13700000008', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'institution_admin', 1, NOW(), NOW()),
(24, '虹口救助站管理员', '13700000009', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'institution_admin', 1, NOW(), NOW()),
(25, '普陀救助站管理员', '13700000010', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'institution_admin', 1, NOW(), NOW());

-- 志愿者（5人，id=26~30）
INSERT INTO user (id, name, phone, password, role, status, created_time, updated_time) VALUES
(26, '志愿者小王', '13600000001', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'volunteer', 1, NOW(), NOW()),
(27, '志愿者小李', '13600000002', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'volunteer', 1, NOW(), NOW()),
(28, '志愿者小张', '13600000003', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'volunteer', 1, NOW(), NOW()),
(29, '志愿者小林', '13600000004', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'volunteer', 1, NOW(), NOW()),
(30, '志愿者小赵', '13600000005', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'volunteer', 1, NOW(), NOW());

-- 宠物医院（4人，id=31~34）
INSERT INTO user (id, name, phone, password, role, status, created_time, updated_time) VALUES
(31, '上海宠物医院',   '13500000001', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'pet_hospital', 1, NOW(), NOW()),
(32, '浦东宠物诊所',   '13500000002', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'pet_hospital', 1, NOW(), NOW()),
(33, '静安宠物中心',   '13500000003', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'pet_hospital', 1, NOW(), NOW()),
(34, '上海爱康宠物医院','13500000004', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'pet_hospital', 1, NOW(), NOW());

-- ================================================
-- 2. 领养人扩展信息（adopter表）
-- ================================================
INSERT INTO adopter (user_id, id_card, address, experience, created_time) VALUES
(3,  '310101199001011234', '上海市静安区南京西路1688弄',    '养猫5年，目前家里有1只英短'),
(4,  '310104198802022345', '上海市徐汇区衡山路516号',        '养狗8年，有金毛饲养经验'),
(5,  '310112199505033456', '上海市闵行区莘庄镇申北路',      '第一次养宠，非常喜欢小动物'),
(6,  '310105199303044567', '上海市长宁区虹桥路2285号',      '有养兔经验，家里有院子'),
(7,  '310106198901055678', '上海市静安区北京西路1068号',    '养猫3年，对猫过敏但很喜欢'),
(8,  '310101199208016789', '上海市黄浦区南京东路步行街',    '家有小孩，想领养温顺猫咪'),
(9,  '310110199407027890', '上海市杨浦区五角场镇国定路',    '退休人员，时间充裕'),
(10, '310107199312038901', '上海市普陀区曹杨路200号',       '养宠新手，但非常有爱心'),
(11, '310115199601049012', '上海市浦东新区陆家嘴环路',      '独居青年，喜欢安静猫咪'),
(12, '310113198705050123', '上海市宝山区顾村镇潘广路',     '养狗10年，有丰富经验'),
(13, '310109199811061234', '上海市虹口区四川北路1500号',    '学生，住在学校宿舍但有寄养条件'),
(14, '310117199002072345', '上海市松江区大学城文汇路',      '有两只猫，再领养一只作伴'),
(15, '310104199606083456', '上海市徐汇区漕河泾开发区',      '租房但房东允许养宠');

-- ================================================
-- 3. 志愿者扩展信息（volunteer表）
-- ================================================
INSERT INTO volunteer (user_id, service_area, task_types, created_time) VALUES
(26, '上海市浦东新区', '救助,照护,运输', NOW()),
(27, '上海市徐汇区',   '救助,医疗协助,照护', NOW()),
(28, '上海市闵行区',   '运输,照护,活动支持', NOW()),
(29, '上海市静安区',   '照护,临时寄养', NOW()),
(30, '上海市宝山区',   '救助,运输,照护', NOW());

-- ================================================
-- 4. 宠物医院扩展信息（hospital表）
-- ================================================
INSERT INTO hospital (user_id, hospital_name, address, license, created_time) VALUES
(31, '上海宠物医院',     '上海市徐汇区漕河泾开发区', '沪宠医证字[2024]001号', NOW()),
(32, '浦东宠物诊所',     '上海市浦东新区张江高科技园区', '沪宠医证字[2024]002号', NOW()),
(33, '静安宠物中心',     '上海市静安区共和新路2800号', '沪宠医证字[2024]003号', NOW()),
(34, '上海爱康宠物医院', '上海市杨浦区黄兴路1200号',   '沪宠医证字[2024]004号', NOW());

-- ================================================
-- 5. 救助所数据（28个，覆盖上海全部16个区，2026-04-02扩充）
-- ================================================
INSERT INTO shelter (id, name, address, lat, lon, max_capacity, current_capacity, region_code, phone, status, medical_level, description, entry_status, audit_status, created_time, updated_time) VALUES
-- 浦东新区（3个）
(1,  '浦东动物救助中心',    '上海市浦东新区浦东大道1234号',   31.2247, 121.5437, 200, 85,  '310115', '021-58001234', 0, 3, '上海市最大的动物救助中心，拥有专业医疗团队和大型救助基地',               1, 1, NOW(), NOW()),
(2,  '浦东爱心宠物之家',    '上海市浦东新区张江高科技园区',  31.2054, 121.5689, 100, 45,  '310115', '021-58002345', 0, 2, '专注流浪猫救助，拥有完善的领养服务体系',                                       1, 1, NOW(), NOW()),
(3,  '川沙流浪动物收容所',  '上海市浦东新区川沙镇新川路',    31.1919, 121.6716, 150, 120, '310115', '021-58003456', 1, 1, '政府合作的流浪动物收容中心',                                                   1, 1, NOW(), NOW()),
-- 徐汇区（2个）
(4,  '徐汇区宠物救助站',    '上海市徐汇区漕河泾开发区',      31.1691, 121.4230, 120, 60,  '310104', '021-64001234', 0, 2, '位于市中心的专业宠物救助机构',                                                 1, 1, NOW(), NOW()),
(5,  '上海小动物保护协会',  '上海市徐汇区衡山路516号',        31.2089, 121.4469, 80,  35,  '310104', '021-64002345', 0, 1, '民间动物保护组织，专注流浪猫狗救助',                                           1, 1, NOW(), NOW()),
-- 闵行区（2个）
(6,  '闵行区流浪动物救助中心','上海市闵行区莘庄镇申北路200号',31.1056, 121.3776, 180, 90,  '310112', '021-64003456', 0, 2, '闵行区官方救助中心，设施完善',                                                 1, 1, NOW(), NOW()),
(7,  '闵行爱心动物之家',    '上海市闵行区七宝镇沪松公路',    31.1524, 121.3411, 60,  25,  '310112', '021-64004567', 0, 1, '社区型动物救助站，服务周边居民',                                               1, 1, NOW(), NOW()),
-- 静安区（2个）
(8,  '静安区宠物服务中心',  '上海市静安区北京西路1068号',    31.2325, 121.4544, 80,  40,  '310106', '021-63101234', 0, 2, '位于市中心的综合性宠物服务机构',                                              1, 1, NOW(), NOW()),
(9,  '上海流浪动物临时收容站','上海市静安区共和新路2800号',  31.2567, 121.4634, 100, 55,  '310106', '021-63102345', 0, 1, '紧急救助临时收容点',                                                           1, 1, NOW(), NOW()),
-- 长宁区（2个）
(10, '长宁区动物救助站',    '上海市长宁区虹桥路2285号',      31.2018, 121.3929, 90,  42,  '310105', '021-62101234', 0, 2, '长宁区专业动物救助机构',                                                       1, 1, NOW(), NOW()),
(11, '上海宠物爱心联盟',    '上海市长宁区中山公园附近',      31.2241, 121.4178, 70,  30,  '310105', '021-62102345', 0, 1, '志愿者运营的爱心救助组织',                                                     1, 1, NOW(), NOW()),
-- 黄浦区（1个）
(12, '黄浦区宠物之家',      '上海市黄浦区南京东路步行街',    31.2317, 121.4832, 50,  22,  '310101', '021-63103456', 0, 1, '市中心小型救助站',                                                             1, 1, NOW(), NOW()),
-- 杨浦区（1个）
(13, '杨浦区流浪动物保护中心','上海市杨浦区五角场镇国定路',  31.3031, 121.5189, 110, 50,  '310110', '021-65101234', 0, 2, '杨浦区官方动物保护中心',                                                       1, 1, NOW(), NOW()),
-- 普陀区（1个）
(14, '普陀区宠物救助站',    '上海市普陀区曹杨路200号',       31.2428, 121.3912, 85,  38,  '310107', '021-62801234', 0, 1, '普陀区爱心救助站',                                                              1, 1, NOW(), NOW()),
-- 虹口区（1个）
(15, '虹口区小动物救助中心', '上海市虹口区四川北路1500号',   31.2642, 121.4923, 75,  32,  '310109', '021-63201234', 0, 1, '虹口区志愿者救助组织',                                                          1, 1, NOW(), NOW()),
-- 宝山区（1个）
(16, '宝山区流浪动物收容所', '上海市宝山区顾村镇潘广路',      31.4028, 121.3542, 130, 65,  '310113', '021-56101234', 0, 2, '宝山区大型动物收容中心',                                                         1, 1, NOW(), NOW()),
-- 嘉定区（1个）
(17, '嘉定区宠物救助中心',  '上海市嘉定区安亭镇曹安公路',   31.3742, 121.1689, 95,  45,  '310114', '021-59501234', 0, 1, '嘉定区动物救助站',                                                              1, 1, NOW(), NOW()),
-- 松江区（1个）
(18, '松江区流浪动物之家',  '上海市松江区大学城文汇路',      31.0317, 121.2289, 100, 48,  '310117', '021-57701234', 0, 1, '松江区爱心救助站',                                                              1, 1, NOW(), NOW()),
-- 青浦区（1个）
(19, '青浦区动物救助站',    '上海市青浦区徐泾镇沪青平公路', 31.1592, 121.3089, 110, 52,  '310118', '021-69201234', 0, 2, '青浦区综合救助中心',                                                           1, 1, NOW(), NOW()),
-- 奉贤区（1个）
(20, '奉贤区流浪动物收容中心','上海市奉贤区南桥镇解放路',   30.9189, 121.4589, 120, 58,  '310120', '021-57401234', 0, 1, '奉贤区动物收容站',                                                              1, 1, NOW(), NOW()),
-- ================================================
-- 新增救助站（2026-04-02，覆盖崇明/金山/各区均衡分布）
-- ================================================
(21, '金山区流浪动物救助站',  '上海市金山区石化卫清西路188号',  30.7412, 121.3312, 80,   35,  '310116', '021-57901234', 0, 1, '金山区专业动物救助站，设施完善，服务周边居民',                                  1, 1, NOW(), NOW()),
(22, '崇明区动物保护中心',    '上海市崇明区城桥镇人民路88号',    31.6234, 121.4056, 90,   40,  '310151', '021-59601234', 0, 1, '崇明区官方动物保护中心，生态救助特色',                                         1, 1, NOW(), NOW()),
(23, '临港新城宠物之家',      '上海市浦东新区临港新城环湖西三路', 30.9189, 121.9078, 60,   20,  '310115', '021-58004567', 0, 2, '临港新城社区宠物救助站，志愿者运营',                                             1, 1, NOW(), NOW()),
(24, '虹桥商务区宠物中心',    '上海市闵行区申长路288号',         31.1901, 121.3156, 70,   28,  '310112', '021-64005678', 0, 2, '虹桥商务区专业宠物服务中心，交通便利',                                         1, 1, NOW(), NOW()),
(25, '北外滩动物救助站',      '上海市虹口区东大名路500号',        31.2734, 121.5023, 55,   22,  '310109', '021-63205678', 0, 1, '北外滩社区动物救助站，服务航运社区居民',                                       1, 1, NOW(), NOW()),
(26, '大宁地区宠物收容所',    '上海市静安区共和新路3800号',       31.2856, 121.4456, 50,   18,  '310106', '021-63105678', 0, 1, '大宁地区社区宠物收容站，专注社区流浪动物救助',                                   1, 1, NOW(), NOW()),
(27, '张江高科技宠物站',      '上海市浦东新区张江高科技园区碧波路',31.2101, 121.6078, 65,   25,  '310115', '021-58006789', 0, 3, '张江高科技园区专业宠物医疗站，拥有先进医疗设备',                                 1, 1, NOW(), NOW()),
(28, '南汇新城动物之家',      '上海市浦东新区南汇新城镇申港大道',  30.8823, 121.8578, 75,   30,  '310115', '021-58007890', 0, 1, '南汇新城社区动物之家，志愿者活跃',                                                 1, 1, NOW(), NOW());

-- ================================================
-- 6. 宠物数据（32只，猫12只+狗14只+小动物6只）
--    institution_id = 16~25 对应各区救助站管理员
-- ================================================
-- --- 猫咪（12只）---
INSERT INTO pet (id, name, breed, age, gender, weight, color, health_status, personality, rescue_date, rescue_location, image_url, status, institution_id, description, rescue_lon, rescue_lat, urgency_level, expected_adoption_time, created_time, updated_time, category_type) VALUES
-- ID 1-4: 中华田园猫系
(1,  '橘子',   '中华田园猫',    10, 1, 4.2, '橘白',    '健康，已绝育，已驱虫',          '温顺亲人，喜欢被摸头',                      DATE_SUB(NOW(), INTERVAL 6 DAY),   '上海市徐汇区衡山路',           'https://images.unsplash.com/photo-1574158622682-e40e69881006?w=600&q=80', 0, 17, '橘白相间的小帅哥，非常亲人，已绝育，适合家庭领养', 121.4469, 31.2089, 0, 1, NOW(), NOW(), 'CATS'),
(2,  '煤球',   '中华田园猫',     8, 1, 4.5, '纯黑色',  '健康，已驱虫，无寄生虫',         '高冷但会撒娇，认主后很粘人',                DATE_SUB(NOW(), INTERVAL 4 DAY),   '上海市浦东新区陆家嘴',         'https://images.unsplash.com/photo-1519052537078-e6302a4968cc?w=600&q=80', 0, 16, '纯黑田园猫，非常帅气，高冷但内心温柔，适合有爱心的人士', 121.5437, 31.2247, 1, 1, NOW(), NOW(), 'CATS'),
(3,  '小狸',   '狸花猫',          4, 1, 2.5, '狸花色',  '健康状况良好',                  '活泼好动，跑酷冠军',                        DATE_SUB(NOW(), INTERVAL 2 DAY),   '上海市闵行区莘庄镇',           'https://images.unsplash.com/photo-1495360010541-f48722b34f7d?w=600&q=80', 0, 18, '年幼狸花猫，非常活泼，已做基础体检，身体倍儿棒', 121.3776, 31.1056, 2, 1, NOW(), NOW(), 'CATS'),
(4,  '花花',   '三花猫',          6, 2, 2.8, '三花色',  '健康，已绝育',                  '调皮可爱，捕虫高手',                        DATE_SUB(NOW(), INTERVAL 3 DAY),   '上海市静安区南京西路',         'https://images.unsplash.com/photo-1513245543132-31f507417b26?w=600&q=80', 0, 19, '活泼可爱的三花小母猫，已绝育，非常粘人', 121.4544, 31.2325, 1, 1, NOW(), NOW(), 'CATS'),

-- ID 5-8: 英短/美短系
(5,  '雪球',   '英国短毛猫',    18, 2, 4.8, '纯白',    '健康，心脏彩超正常',            '性格安静，喜欢独处',                        DATE_SUB(NOW(), INTERVAL 14 DAY),  '上海市静安区北京西路',         'https://images.unsplash.com/photo-1543852786-1cf6624b9987?w=600&q=80', 0, 19, '纯白英短，蓝眼睛，非常漂亮安静，适合独居人士', 121.4544, 31.2325, 0, 2, NOW(), NOW(), 'CATS'),
(6,  '奶油',   '英国短毛猫',    24, 2, 5.0, '蓝金色',  '健康，已绝育',                  '安静乖巧，非常粘人',                        DATE_SUB(NOW(), INTERVAL 20 DAY),  '上海市徐汇区漕河泾',           'https://images.unsplash.com/photo-1592194996308-7b43878e84a6?w=600&q=80', 0, 17, '蓝金色英国短毛猫，圆圆的脸蛋，性格温顺', 121.4230, 31.1691, 0, 2, NOW(), NOW(), 'CATS'),
(7,  '虎子',   '美国短毛猫',    12, 1, 4.3, '银虎斑',  '健康，已驱虫',                  '活泼好动，喜欢玩逗猫棒',                    DATE_SUB(NOW(), INTERVAL 9 DAY),   '上海市长宁区虹桥路',           'https://images.unsplash.com/photo-1606208427126-a7f7f3d3e616?w=600&q=80', 0, 20, '银虎斑美短，非常活泼可爱，适合有小孩的家庭', 121.3929, 31.2018, 0, 1, NOW(), NOW(), 'CATS'),
(8,  '小灰',   '美国短毛猫',     6, 2, 3.2, '棕虎斑',  '健康状况良好',                  '好奇心强，爱探索',                          DATE_SUB(NOW(), INTERVAL 5 DAY),   '上海市浦东新区张江',           'https://images.unsplash.com/photo-1518791841217-8f162f1e1131?w=600&q=80', 0, 16, '棕虎斑美短宝宝，好奇心很重，适应能力强', 121.5689, 31.2054, 0, 1, NOW(), NOW(), 'CATS'),

-- ID 9-12: 特殊品种
(9,  '团子',   '波斯猫',        30, 2, 4.5, '白色',    '健康，已绝育，需定期梳毛',       '安静温顺，像个毛绒球',                       DATE_SUB(NOW(), INTERVAL 30 DAY),  '上海市黄浦区南京东路',         'https://images.unsplash.com/photo-1573865526739-10659fec78a5?w=600&q=80', 0, 21, '纯白波斯猫，毛发飘逸，非常温顺安静', 121.4832, 31.2317, 0, 3, NOW(), NOW(), 'CATS'),
(10, '汤圆',   '布偶猫',        14, 2, 4.2, '海豹色',  '健康状况良好',                  '温柔亲人，被称为猫中仙女',                   DATE_SUB(NOW(), INTERVAL 11 DAY),  '上海市黄浦区外滩',             'https://images.unsplash.com/photo-1543466835-00a7907e9de1?w=600&q=80', 0, 12, '海豹色布偶猫，毛发飘逸如仙女，非常温顺', 121.4900, 31.2397, 0, 2, NOW(), NOW(), 'CATS'),
(11, '奶茶',   '暹罗猫',         8, 1, 3.8, '海豹色',  '健康，已绝育',                  '话唠，喜欢叫，超级粘人',                     DATE_SUB(NOW(), INTERVAL 7 DAY),   '上海市杨浦区五角场',           'https://images.unsplash.com/photo-1595944031759-74d8c9d58a58?w=600&q=80', 0, 22, '暹罗猫，超级粘人的小话唠，喜欢和人互动', 121.5189, 31.3031, 0, 1, NOW(), NOW(), 'CATS'),
(12, '小年',   '无毛猫',        12, 1, 3.5, '粉色',    '健康状况良好，需保暖',           '极其粘人，喜欢贴着你睡',                     DATE_SUB(NOW(), INTERVAL 15 DAY),  '上海市普陀区曹杨路',           'https://images.unsplash.com/photo-1511044568932-338cba0ad803?w=600&q=80', 0, 25, '无毛猫，非常特别，冬天需要穿衣服，适合有耐心的主人', 121.3912, 31.2428, 0, 2, NOW(), NOW(), 'CATS'),

-- --- 狗狗（14只）---
-- ID 13-16: 金毛/拉布拉多系
(13, '旺财',   '金毛寻回犬',    24, 1, 25.5, '金黄色', '健康状况良好，已绝育',           '性格温顺，对人友好，适合家庭',               DATE_SUB(NOW(), INTERVAL 15 DAY),  '上海市浦东新区张江路',         'https://images.unsplash.com/photo-1633722715463-d30f4f325e24?w=600&q=80', 0, 16, '纯种金毛，性格极其温顺，非常适合有小孩的家庭', 121.5689, 31.2054, 0, 2, NOW(), NOW(), 'DOGS'),
(14, '大黄',   '金毛寻回犬',    48, 1, 30.0, '金黄色', '健康，有轻微皮肤病已治愈',       '性格温顺，对儿童友好',                        DATE_SUB(NOW(), INTERVAL 25 DAY),  '上海市宝山区顾村',             'https://images.unsplash.com/photo-1552053831-71594a27632d?w=600&q=80', 0, 23, '大金毛，性格极其温顺，非常适合有小孩的家庭', 121.3542, 31.4028, 0, 3, NOW(), NOW(), 'DOGS'),
(15, '小七',   '拉布拉多',      36, 2, 26.0, '奶油色', '健康状况良好，聪明活泼',         '聪明活泼，学习能力极强',                      DATE_SUB(NOW(), INTERVAL 16 DAY),  '上海市嘉定区安亭',             'https://images.unsplash.com/photo-1561037404-61cd46aa615b?w=600&q=80', 0, 17, '奶油色拉布拉多，非常聪明，曾是导盲犬候选', 121.1689, 31.3742, 0, 2, NOW(), NOW(), 'DOGS'),
(16, '黑豹',   '拉布拉多',      48, 1, 28.0, '黑色',   '健康，定期体检',                 '聪明听话，服从性高',                          DATE_SUB(NOW(), INTERVAL 30 DAY),  '上海市宝山区顾村镇',           'https://images.unsplash.com/photo-1529429617124-95b109e86571?w=600&q=80', 0, 23, '黑色拉布拉多，非常聪明稳重，适合有经验的主人', 121.3542, 31.4028, 0, 3, NOW(), NOW(), 'DOGS'),

-- ID 17-20: 小型犬
(17, '阿福',   '柴犬',          30, 1, 10.5, '赤色',   '健康状况良好',                  '性格倔强但非常可爱，表情包本包',              DATE_SUB(NOW(), INTERVAL 8 DAY),   '上海市杨浦区五角场',           'https://images.unsplash.com/photo-1587300003388-59208cc962cb?w=600&q=80', 0, 22, '赤色柴犬，表情包本包，非常治愈，适合年轻人', 121.5189, 31.3031, 0, 1, NOW(), NOW(), 'DOGS'),
(18, '豆豆',   '泰迪犬',        18, 1, 5.5,  '棕色',   '健康，已做美容',                 '粘人可爱，像棉花糖一样',                      DATE_SUB(NOW(), INTERVAL 8 DAY),   '上海市黄浦区南京路',           'https://images.unsplash.com/photo-1616149702344-c48c0c07f756?w=600&q=80', 0, 21, '棕色泰迪，非常粘人，适合公寓饲养', 121.4832, 31.2317, 1, 1, NOW(), NOW(), 'DOGS'),
(19, '小雪',   '萨摩耶',        28, 2, 22.0, '白色',   '健康状况良好',                  '微笑天使，非常活泼',                          DATE_SUB(NOW(), INTERVAL 20 DAY),  '上海市虹口区四川北路',         'https://images.unsplash.com/photo-1605568427561-40dd23c2acea?w=600&q=80', 0, 24, '萨摩耶，毛发雪白，会微笑的天使狗狗', 121.4923, 31.2642, 1, 2, NOW(), NOW(), 'DOGS'),
(20, '贝塔',   '比熊犬',        20, 1, 6.5,  '白色',   '健康，已做美容',                 '活泼可爱，像棉花糖一样',                      DATE_SUB(NOW(), INTERVAL 5 DAY),   '上海市普陀区曹杨路',           'https://images.unsplash.com/photo-1583337130417-3346a1be7dee?w=600&q=80', 0, 25, '白色比熊，像一团棉花糖，非常适合公寓饲养', 121.3912, 31.2428, 0, 1, NOW(), NOW(), 'DOGS'),

-- ID 21-24: 中大型犬
(21, '笨笨',   '哈士奇',        30, 1, 22.0, '灰白色', '健康状况良好，精力旺盛',        '拆家达人，但非常呆萌',                        DATE_SUB(NOW(), INTERVAL 12 DAY),  '上海市松江区大学城',           'https://images.unsplash.com/photo-1529432265830-96fd8b2ce7d7?w=600&q=80', 0, 18, '二哈本哈，精力旺盛，呆萌可爱，需要大量运动', 121.2289, 31.0317, 1, 2, NOW(), NOW(), 'DOGS'),
(22, '球球',   '柯基',          36, 2, 12.0, '黄白两色','健康状况良好',                 '腿短屁股圆，非常可爱',                        DATE_SUB(NOW(), INTERVAL 13 DAY),  '上海市松江区大学城',           'https://images.unsplash.com/photo-1612536057832-2ff7ead58194?w=600&q=80', 0, 18, '黄白柯基，小短腿大屁股，走路很萌', 121.2289, 31.0317, 0, 2, NOW(), NOW(), 'DOGS'),
(23, '小白',   '萨摩耶',        20, 2, 20.5, '白色',   '健康状况良好',                  '爱笑的天使，超级粘人',                        DATE_SUB(NOW(), INTERVAL 10 DAY),  '上海市闵行区七宝镇',           'https://images.unsplash.com/photo-1526057565006-20beab8dd2ed?w=600&q=80', 0, 18, '萨摩耶，微笑天使，毛发雪白，非常治愈', 121.3411, 31.1524, 0, 1, NOW(), NOW(), 'DOGS'),
(24, '小黑',   '中华田园犬',    12, 1, 15.2, '黑色',   '健康，已接种疫苗',               '性格活泼，聪明机灵，适应能力强',              DATE_SUB(NOW(), INTERVAL 4 DAY),   '上海市浦东新区川沙镇',         'https://images.unsplash.com/photo-1477884213360-7e9d7dcc1e48?w=600&q=80', 0, 16, '黑色田园犬，非常机灵，被志愿者救助', 121.6716, 31.1919, 1, 2, NOW(), NOW(), 'DOGS'),
(25, '阿黄',   '中华田园犬',    18, 1, 14.0, '黄色',   '健康状况良好',                  '忠诚护家，对主人非常依恋',                    DATE_SUB(NOW(), INTERVAL 7 DAY),   '上海市奉贤区南桥镇',           'https://images.unsplash.com/photo-1588276036537-27d507c93b8a?w=600&q=80', 0, 20, '黄色田园犬，性格忠诚，适合有院子的家庭', 121.4589, 30.9189, 0, 2, NOW(), NOW(), 'DOGS'),
(26, '多多',   '边境牧羊犬',    24, 1, 18.0, '黑白两色','健康状况良好，极其聪明',        '最聪明的犬种，学习能力超强',                  DATE_SUB(NOW(), INTERVAL 18 DAY),  '上海市青浦区徐泾镇',           'https://images.unsplash.com/photo-1548199973-03cce0bbc87b?w=600&q=80', 0, 19, '边境牧羊犬，IQ爆表，学习指令超级快', 121.3089, 31.1592, 0, 2, NOW(), NOW(), 'DOGS'),

-- --- 小动物（6只）---
-- ID 27-29: 兔兔
(27, '毛茸茸', '荷兰垂耳兔',     8, 2, 1.8, '白色',   '健康状况良好',                  '温顺安静，适合互动',                          DATE_SUB(NOW(), INTERVAL 7 DAY),   '上海市长宁区中山公园',         'https://images.unsplash.com/photo-1585110396000-c9ffd4e4b308?w=600&q=80', 0, 20, '荷兰垂耳兔，耳朵垂垂的，非常可爱', 121.4178, 31.2241, 0, 1, NOW(), NOW(), 'SMALL_ANIMALS'),
(28, '小白兔', '新西兰白兔',     6, 1, 2.0, '白色',   '健康，已绝育',                  '性格温顺，爱吃胡萝卜',                        DATE_SUB(NOW(), INTERVAL 4 DAY),   '上海市静安区北京西路',         'https://images.unsplash.com/photo-1436891620584-47fd0e565afb?w=600&q=80', 0, 19, '纯白肉兔，非常温顺，适合有耐心的小朋友', 121.4544, 31.2325, 0, 1, NOW(), NOW(), 'SMALL_ANIMALS'),
(29, '灰灰',   '安哥拉兔',      10, 1, 2.5, '灰色',   '健康状况良好',                  '毛发很长，需要定期打理',                       DATE_SUB(NOW(), INTERVAL 10 DAY),  '上海市虹口区四川北路',         'https://images.unsplash.com/photo-1452857297128-d9c29adba80b?w=600&q=80', 0, 24, '安哥拉兔，毛发蓬松像朵云，非常特别', 121.4923, 31.2642, 0, 2, NOW(), NOW(), 'SMALL_ANIMALS'),

-- ID 30-32: 仓鼠/荷兰猪
(30, '小瓜子', '金丝熊仓鼠',     3, 1, 0.15, '金黄色', '健康状况良好',                  '活泼好动，跑轮子冠军',                        DATE_SUB(NOW(), INTERVAL 2 DAY),   '上海市奉贤区南桥',             'https://images.unsplash.com/photo-1425082661705-1834bfd09dca?w=600&q=80', 0, 20, '金丝熊仓鼠，圆滚滚的非常可爱', 121.4589, 30.9189, 0, 1, NOW(), NOW(), 'SMALL_ANIMALS'),
(31, '小团子', '仓鼠（三线）',   4, 2, 0.12, '三线色', '健康状况良好',                  '性格温顺，喜欢藏食物',                         DATE_SUB(NOW(), INTERVAL 3 DAY),   '上海市闵行区莘庄',             'https://images.unsplash.com/photo-1425082661705-1834bfd09dca?w=600&q=80', 0, 18, '三线仓鼠，体型小巧，非常可爱', 121.3776, 31.1056, 0, 1, NOW(), NOW(), 'SMALL_ANIMALS'),
(32, '荷兰猪猪','荷兰猪',        6, 2, 0.9, '黄白',   '健康状况良好',                  '性格温顺，喂食时会发出可爱叫声',               DATE_SUB(NOW(), INTERVAL 5 DAY),   '上海市青浦区徐泾',             'https://images.unsplash.com/photo-1548767797-d8c844163c4c?w=600&q=80', 0, 19, '荷兰猪，性格温顺，喂食时会咕咕叫', 121.3089, 31.1592, 0, 1, NOW(), NOW(), 'SMALL_ANIMALS');

-- ================================================
-- 7. 领养申请（15条，覆盖所有审核状态）
-- ================================================
INSERT INTO adoption_application (applicant_id, pet_id, apply_date, status, review_comment, review_time, family_members, living_conditions, work_status, experience) VALUES
-- 状态0：待审核
(3,  1,  DATE_SUB(NOW(), INTERVAL 1 DAY), 0, NULL, NULL,                        '独居，无其他宠物',      '上海市静安区南京西路，老公房90平',      '在读研究生，时间充裕', '养猫5年，家里有1只英短'),
(4,  13, DATE_SUB(NOW(), INTERVAL 2 DAY), 0, NULL, NULL,                        '一家四口，孩子10岁',    '上海市徐汇区，商品房120平，有院子',    '国企员工，福利好',     '养狗8年，有金毛饲养经验'),
(5,  3,  DATE_SUB(NOW(), INTERVAL 3 DAY), 0, NULL, NULL,                        '单身独居',             '上海市闵行区莘庄，公寓50平',           '应届毕业生，工作稳定', '第一次养宠，但非常喜欢小动物'),
(6,  27, DATE_SUB(NOW(), INTERVAL 1 DAY), 0, NULL, NULL,                        '家有8岁小朋友',        '上海市长宁区，花园洋房150平',         '全职妈妈',             '有养兔经验，家里有院子'),
-- 状态1：已通过
(7,  4,  DATE_SUB(NOW(), INTERVAL 5 DAY), 1, '材料审核通过，欢迎领养', DATE_SUB(NOW(), INTERVAL 4 DAY), '退休夫妻，无其他宠物', '上海市静安区北京西路，老公房90平', '退休，有充足时间', '养宠经验丰富'),
(8,  15, DATE_SUB(NOW(), INTERVAL 7 DAY), 1, '审核通过，请尽快来领养', DATE_SUB(NOW(), INTERVAL 6 DAY), '单身公寓，无宠物',     '上海市嘉定区，商品房95平',           'IT工程师，远程办公', '养宠新手，但非常喜欢金毛'),
(9,  7,  DATE_SUB(NOW(), INTERVAL 4 DAY), 1, '通过审核，请安排家访',   DATE_SUB(NOW(), INTERVAL 3 DAY), '一家三口',           '上海市长宁区，电梯房100平',         '教师，假期多',       '养猫2年'),
-- 状态2：已拒绝
(10, 26, DATE_SUB(NOW(), INTERVAL 10 DAY), 2, '居住条件不符合要求，无法领养大型犬', DATE_SUB(NOW(), INTERVAL 8 DAY), '合租，室友反对', '上海市闵行区，合租房30平', '刚毕业学生', '无养宠经验'),
(11, 9,  DATE_SUB(NOW(), INTERVAL 8 DAY), 2, '未通过审核，猫咪需定期梳毛，您的时间安排不符合', DATE_SUB(NOW(), INTERVAL 6 DAY), '出差频繁', '经常出差，无法照顾', '销售，经常出差', '无养猫经验'),
-- 状态3：待补充材料
(12, 14, DATE_SUB(NOW(), INTERVAL 3 DAY), 3, '请补充家访视频和身份证复印件', DATE_SUB(NOW(), INTERVAL 2 DAY), '一家四口', '上海市宝山区顾村，花园洋房200平', '企业高管，时间灵活', '养宠经验丰富'),
(13, 22, DATE_SUB(NOW(), INTERVAL 2 DAY), 3, '需补充居住环境照片', DATE_SUB(NOW(), INTERVAL 1 DAY),    '单身，有稳定住所', '上海市松江区，公寓80平',            '国企员工',           '养宠新手'),
-- 状态4：待签署协议
(14, 5,  DATE_SUB(NOW(), INTERVAL 8 DAY), 4, '审核通过，请签署领养协议', DATE_SUB(NOW(), INTERVAL 7 DAY),  '单身，有稳定住所', '上海市徐汇区，商品房95平',         '国企员工',           '养猫3年'),
(15, 10, DATE_SUB(NOW(), INTERVAL 5 DAY), 4, '审核通过，等待签署协议', DATE_SUB(NOW(), INTERVAL 4 DAY),   '独居，无反对意见', '上海市黄浦区，单身公寓60平',      '设计师，居家办公',   '养宠2年'),
-- 状态5：已完成（已签署协议）
(3,  6,  DATE_SUB(NOW(), INTERVAL 15 DAY), 5, '已完成领养，欢迎新主人', DATE_SUB(NOW(), INTERVAL 14 DAY), '独居', '上海市静安区，公寓70平', '研究生，时间充裕', '养猫5年');

-- ================================================
-- 8. 回访记录
-- ================================================
INSERT INTO follow_up_record (adoption_id, follow_up_date, follower_id, content, pet_condition, images, created_time) VALUES
(18, DATE_SUB(NOW(), INTERVAL 3 DAY),  26, '领养后第三天回访：猫咪适应良好，已开始在新家探索。领养人陈建国表示非常满意。', '小橘（英短改名后）健康状况良好，已适应新环境，精神活泼', NULL, NOW()),
(19, DATE_SUB(NOW(), INTERVAL 1 DAY), 27, '领养后一周回访：拉布拉多小七与领养人相处融洽，每天早晚遛弯，性格温顺。',     '小七健康状况良好，体重稳定，精神状态佳，与家人相处融洽', NULL, NOW()),
(20, DATE_SUB(NOW(), INTERVAL 5 DAY), 28, '领养后两周回访：美短虎子已经完全适应新家，喜欢和主人互动，性格活泼。',       '虎子健康状况良好，体重3.8kg，非常活泼粘人', NULL, NOW()),
(21, DATE_SUB(NOW(), INTERVAL 2 DAY), 29, '领养人家访：检查了居住环境，领养意向强烈，有充足时间照顾宠物。',            '花花健康状况良好，正等待领养', NULL, NOW());

-- ================================================
-- 9. 健康档案（30条，覆盖各品种宠物）
-- ================================================
INSERT INTO health_record (pet_id, record_type, title, record_date, content, institution, veterinarian, cost, next_reminder_date, remark, status, created_time, updated_time) VALUES
-- 猫咪健康档案
(1,  'vaccine',   '猫三联疫苗第三针',         DATE_SUB(NOW(), INTERVAL 5 DAY),   '完成猫三联疫苗第三针，免疫程序完成，无不良反应',                        '上海宠物医院',      '张医生', 120.00, DATE_ADD(CURDATE(), INTERVAL 365 DAY), '免疫程序已完成', 0, NOW(), NOW()),
(1,  'checkup',   '常规健康体检',             DATE_SUB(NOW(), INTERVAL 30 DAY), '体温正常、心率正常、粪便检查无寄生虫，体重4.2kg标准',                  '上海宠物医院',      '李医生', 150.00, NULL,                            '建议每季度体检一次', 0, NOW(), NOW()),
(2,  'vaccine',   '狂犬疫苗接种',             DATE_SUB(NOW(), INTERVAL 8 DAY),   '接种进口狂犬疫苗，保护期1年，无不良反应',                                 '浦东宠物诊所',      '王医生',  80.00, DATE_ADD(CURDATE(), INTERVAL 357 DAY), '请保存好疫苗本', 0, NOW(), NOW()),
(2,  'dental',    '口腔清洁护理',             DATE_SUB(NOW(), INTERVAL 60 DAY), '超声波洁牙，清除牙结石，抛光处理，口腔健康状况良好',                    '上海宠物医院',      '陈医生', 300.00, DATE_ADD(CURDATE(), INTERVAL 305 DAY), '建议每半年洁牙', 0, NOW(), NOW()),
(3,  'vaccine',   '猫三联疫苗第一针',         DATE_SUB(NOW(), INTERVAL 10 DAY), '首次接种猫三联疫苗第一针，接种后观察30分钟无异常',                       '上海宠物医院',      '张医生', 120.00, DATE_ADD(CURDATE(), INTERVAL 20 DAY),  '需在3周后接种第二针', 0, NOW(), NOW()),
(3,  'checkup',   '新猫到家体检',             DATE_SUB(NOW(), INTERVAL 3 DAY),  '体格检查、耳道检查、皮肤检查均正常，非常健康',                           '上海宠物医院',      '李医生', 100.00, NULL,                            '狸花猫身体素质好', 0, NOW(), NOW()),
(4,  'vaccine',   '狂犬疫苗接种',             DATE_SUB(NOW(), INTERVAL 7 DAY),   '接种狂犬疫苗，抗体检测合格',                                              '上海宠物医院',      '张医生',  80.00, DATE_ADD(CURDATE(), INTERVAL 358 DAY), '已完成免疫', 0, NOW(), NOW()),
(4,  'treatment', '皮肤病治疗',               DATE_SUB(NOW(), INTERVAL 20 DAY), '真菌感染，口服药物+药浴治疗，一周后明显好转',                            '上海宠物医院',      '张医生', 350.00, DATE_ADD(CURDATE(), INTERVAL 8 DAY),   '需继续用药', 0, NOW(), NOW()),
(5,  'vaccine',   '年度疫苗加强针',           DATE_SUB(NOW(), INTERVAL 20 DAY), '英短年度疫苗加强接种，抗体水平检测正常',                                  '静安宠物中心',      '赵医生', 150.00, DATE_ADD(CURDATE(), INTERVAL 345 DAY), '抗体水平正常', 0, NOW(), NOW()),
(5,  'checkup',   '心脏彩超检查',             DATE_SUB(NOW(), INTERVAL 40 DAY), '英短常见心脏病筛查，心脏功能正常，无杂音',                               '上海宠物医院',      '心内周医生', 450.00, DATE_ADD(CURDATE(), INTERVAL 325 DAY), '建议定期复查', 0, NOW(), NOW()),
(7,  'vaccine',   '猫三联疫苗第二针',         DATE_SUB(NOW(), INTERVAL 10 DAY), '美短接种猫三联疫苗第二针，无不良反应',                                   '长宁宠物诊所',      '林医生', 120.00, DATE_ADD(CURDATE(), INTERVAL 50 DAY),  '即将接种第三针', 0, NOW(), NOW()),
(9,  'checkup',   '年度全面体检',             DATE_SUB(NOW(), INTERVAL 45 DAY), '波斯猫全面体检，血常规正常，肝肾功能良好，体重标准',                    '上海宠物医院',      '张医生', 280.00, NULL,                            '需定期梳毛护理', 0, NOW(), NOW()),
(10, 'vaccine',   '狂犬疫苗及联苗接种',       DATE_SUB(NOW(), INTERVAL 12 DAY), '布偶猫接种狂犬疫苗和猫三联疫苗，无过敏反应',                            '上海宠物医院',      '王医生', 200.00, DATE_ADD(CURDATE(), INTERVAL 348 DAY), '免疫完成', 0, NOW(), NOW()),

-- 狗狗健康档案
(13, 'vaccine',   '狂犬疫苗及联苗接种',       DATE_SUB(NOW(), INTERVAL 10 DAY), '接种狂犬疫苗和钩端螺旋体疫苗，接种后无不良反应',                        '上海宠物医院',      '张医生', 180.00, DATE_ADD(CURDATE(), INTERVAL 355 DAY), '接种后观察正常', 0, NOW(), NOW()),
(13, 'checkup',   '运动功能评估',             DATE_SUB(NOW(), INTERVAL 30 DAY), '金毛后腿髋关节X光检查，轻度发育不良，建议补充软骨素，控制体重',        '上海宠物骨科中心',  '骨科刘医生', 280.00, DATE_ADD(CURDATE(), INTERVAL 60 DAY), '控制体重+软骨素', 0, NOW(), NOW()),
(14, 'vaccine',   '狂犬疫苗接种',             DATE_SUB(NOW(), INTERVAL 8 DAY),   '接种狂犬疫苗，芯片扫描登记，无不良反应',                                  '宝山宠物诊所',      '马医生',  80.00, DATE_ADD(CURDATE(), INTERVAL 357 DAY), '芯片已登记', 0, NOW(), NOW()),
(14, 'treatment', '皮肤过敏治疗',             DATE_SUB(NOW(), INTERVAL 25 DAY), '食物过敏导致皮肤瘙痒，口服抗过敏药配合药浴，一周后明显好转',            '上海宠物医院',      '张医生', 380.00, DATE_ADD(CURDATE(), INTERVAL 5 DAY),   '需继续观察饮食', 0, NOW(), NOW()),
(15, 'vaccine',   '犬类八联疫苗接种',         DATE_SUB(NOW(), INTERVAL 3 DAY),   '接种犬类八联疫苗，接种后观察正常',                                        '嘉定宠物医院',      '孙医生', 160.00, DATE_ADD(CURDATE(), INTERVAL 362 DAY), '免疫程序正常', 0, NOW(), NOW()),
(15, 'checkup',   '眼睛检查',                 DATE_SUB(NOW(), INTERVAL 50 DAY), '拉布拉多常见白内障筛查，双眼清澈，无遗传性眼病征兆',                    '上海宠物眼科中心',  '眼科李医生', 220.00, DATE_ADD(CURDATE(), INTERVAL 315 DAY), '眼睛健康', 0, NOW(), NOW()),
(17, 'vaccine',   '狂犬疫苗接种',             DATE_SUB(NOW(), INTERVAL 12 DAY), '接种狂犬疫苗，办理犬类免疫证明',                                         '杨浦宠物医院',      '陈医生',  80.00, DATE_ADD(CURDATE(), INTERVAL 353 DAY), '犬证已办理', 0, NOW(), NOW()),
(17, 'checkup',   '年度体检',                 DATE_SUB(NOW(), INTERVAL 60 DAY), '柴犬年度全面体检，血常规正常，肝肾功能良好，体重标准',                  '杨浦宠物医院',      '陈医生', 260.00, NULL,                            '身体非常健康', 0, NOW(), NOW()),
(18, 'vaccine',   '狂犬疫苗接种',             DATE_SUB(NOW(), INTERVAL 180 DAY), '接种狂犬疫苗，即将到期，请及时续种',                                     '黄浦宠物诊所',      '赵医生',  80.00, DATE_ADD(CURDATE(), INTERVAL 185 DAY), '即将到期', 1, NOW(), NOW()),
(18, 'dental',    '牙齿护理',                  DATE_SUB(NOW(), INTERVAL 120 DAY), '牙结石清理，牙龈护理',                                                   '黄浦宠物诊所',      '赵医生', 280.00, DATE_ADD(CURDATE(), INTERVAL 245 DAY), '小型犬需定期护理', 0, NOW(), NOW()),
(19, 'vaccine',   '犬类联苗接种',              DATE_SUB(NOW(), INTERVAL 15 DAY), '萨摩耶接种犬类联苗，无不良反应',                                        '虹口宠物诊所',      '陈医生', 160.00, DATE_ADD(CURDATE(), INTERVAL 350 DAY), '免疫完成', 0, NOW(), NOW()),
(21, 'checkup',   '骨骼关节检查',             DATE_SUB(NOW(), INTERVAL 35 DAY), '哈士奇骨骼关节X光检查，关节发育正常，无异常',                           '上海宠物医院',      '张医生', 250.00, NULL,                            '骨骼健康', 0, NOW(), NOW()),
(26, 'vaccine',   '犬八联疫苗接种',            DATE_SUB(NOW(), INTERVAL 7 DAY),  '边牧接种犬八联疫苗，智力测试表现优异',                                   '青浦宠物医院',      '孙医生', 200.00, DATE_ADD(CURDATE(), INTERVAL 358 DAY), '边牧智商超高', 0, NOW(), NOW()),

-- 小动物健康档案
(27, 'checkup',   '兔兔常规体检',              DATE_SUB(NOW(), INTERVAL 10 DAY), '荷兰垂耳兔体检：牙齿正常、体重1.8kg、心肺功能良好',                    '上海宠物医院',      '异宠科赵医生', 120.00, DATE_ADD(CURDATE(), INTERVAL 90 DAY), '兔兔很健康', 0, NOW(), NOW()),
(28, 'vaccine',   '兔瘟疫苗接种',              DATE_SUB(NOW(), INTERVAL 5 DAY), '新购兔瘟疫苗接种，无应激反应',                                           '上海宠物医院',      '异宠科赵医生',  60.00, DATE_ADD(CURDATE(), INTERVAL 180 DAY), '需定期驱虫', 0, NOW(), NOW()),
(30, 'checkup',   '仓鼠健康检查',              DATE_SUB(NOW(), INTERVAL 7 DAY),  '金丝熊体重0.15kg，正常范围，颊囊无积食，毛发光亮',                      '上海宠物医院',      '异宠科林医生',  80.00, NULL,                            '仓鼠非常健康', 0, NOW(), NOW()),
(32, 'checkup',   '荷兰猪常规体检',            DATE_SUB(NOW(), INTERVAL 3 DAY),  '荷兰猪体重0.9kg，牙齿正常，无体外寄生虫，精神活泼',                      '上海宠物医院',      '异宠科林医生',  90.00, NULL,                            '荷兰猪很健康', 0, NOW(), NOW());

-- ================================================
-- 10. 志愿者任务（15条，覆盖所有任务类型和状态）
-- ================================================
INSERT INTO volunteer_task (title, task_type, description, status, priority, pet_id, shelter_id, location, expected_date, completed_date, publish_date, publisher_id, volunteer_id, reward_points, required_people, signed_people, contact_phone, estimated_hours, result, created_time, updated_time) VALUES
-- 待领取任务（6条）
('紧急救助：浦东新区受伤流浪狗',      'rescue',    '在浦东新区张江地铁站附近发现一只后腿受伤的流浪狗，急需志愿者前往救助并送医。',                                        NULL, 2, NULL, 1,  '上海市浦东新区张江地铁站',          DATE_ADD(CURDATE(), INTERVAL 1 DAY),  NULL,  NOW(), 1, NULL, 50, 3, 0, '021-58001234', 3.0, NULL, NOW(), NOW()),
('杨浦区受伤猫咪紧急救助',            'rescue',    '杨浦区五角场小区内发现一只后腿受伤的猫咪，无法行走，需要紧急救助。',                                                NULL, 2, NULL, 13, '上海市杨浦区五角场镇',              DATE_ADD(CURDATE(), INTERVAL 1 DAY),  NULL,  NOW(), 1, NULL, 40, 2, 0, '021-65101234', 2.5, NULL, NOW(), NOW()),
('宠物跨区转运协助',                  'transport', '将3只救助猫咪从浦东救助中心转运至徐汇区宠物救助站，需要志愿者协助装载和运输。',                                      NULL, 1, NULL, 1,  '浦东大道1234号 → 徐汇区漕河泾',     DATE_ADD(CURDATE(), INTERVAL 2 DAY),  NULL,  NOW(), 1, NULL, 35, 3, 0, '021-58001234', 5.0, NULL, NOW(), NOW()),
('救助站日常清洁消毒',                'care',      '浦东救助中心需要志愿者协助进行笼舍清洁、消毒，以及宠物喂食照料工作。',                                          NULL, 0, NULL, 1,  '上海市浦东新区浦东大道1234号',        DATE_ADD(CURDATE(), INTERVAL 3 DAY),  NULL,  NOW(), 1, NULL, 20, 5, 0, '021-58001234', 4.0, NULL, NOW(), NOW()),
('宠物疫苗接种协助',                   'medical',   '本周六救助站集中疫苗接种日，需要志愿者协助医生进行疫苗接种和记录工作。',                                        NULL, 0, NULL, 8,  '上海市静安区共和新路2800号',         DATE_ADD(CURDATE(), INTERVAL 5 DAY),  NULL,  NOW(), 1, NULL, 30, 4, 0, '021-63102345', 3.5, NULL, NOW(), NOW()),
('周末领养日活动支持',                 'event',     '本月第三个周六在静安区举办大型领养日活动，需要志愿者协助布展、接待和现场解说。',                                NULL, 0, NULL, 8,  '上海市静安区北京西路1068号',         DATE_ADD(CURDATE(), INTERVAL 7 DAY),  NULL,  NOW(), 1, NULL, 25, 8, 0, '021-63101234', 6.0, NULL, NOW(), NOW()),

-- 进行中任务（4条）
('受伤金毛术后照护',                  'care',      '旺财刚完成体检，需要志愿者协助术后照护，包括喂药、换药',                                                   NULL, 1, 13,  1,  '上海市浦东新区浦东大道1234号',        DATE_ADD(CURDATE(), INTERVAL 3 DAY),  NULL,  DATE_SUB(NOW(), INTERVAL 2 DAY), 1, 26, 30, 2, 1, '021-58001234', 2.0, NULL, NOW(), NOW()),
('布偶猫社会化训练',                  'care',      '对救助站内的布偶猫汤圆进行社会化训练，帮助它适应人类接触，提高领养率',                                       NULL, 1, 10,  12, '上海市黄浦区南京东路步行街',          DATE_ADD(CURDATE(), INTERVAL 5 DAY),  NULL,  DATE_SUB(NOW(), INTERVAL 1 DAY), 1, 27, 15, 2, 1, '021-63103456', 3.0, NULL, NOW(), NOW()),
('猫咪照护周末活动',                  'care',      '组织志愿者在救助站照护猫咪，包括喂食、清理猫砂、陪伴互动',                                               NULL, 0, NULL, 4,  '上海市徐汇区漕河泾开发区',            DATE_ADD(CURDATE(), INTERVAL 4 DAY),  NULL,  DATE_SUB(NOW(), INTERVAL 3 DAY), 1, 28, 20, 4, 2, '021-64001234', 4.0, NULL, NOW(), NOW()),
('隔离期宠物护理',                    'care',      '新救助宠物需要7天隔离观察期，需要志愿者协助每日喂食、清理和记录健康状况',                                  NULL, 1, NULL, 10, '上海市长宁区虹桥路2285号',            DATE_ADD(CURDATE(), INTERVAL 2 DAY),  NULL,  DATE_SUB(NOW(), INTERVAL 1 DAY), 1, 29, 30, 2, 1, '021-62101234', 2.5, NULL, NOW(), NOW()),

-- 已完成任务（4条）
('流浪猫救助行动',                    'rescue',    '成功救助一只受伤的橘猫，送至浦东救助中心，身体状况恢复良好',                                            NULL, 1, 1,   1,  '上海市浦东新区陆家嘴环路',            DATE_SUB(CURDATE(), INTERVAL 5 DAY),  DATE_SUB(CURDATE(), INTERVAL 5 DAY),  DATE_SUB(NOW(), INTERVAL 10 DAY), 1, 26, 50, 2, 2, '021-58001234', 3.0, '成功救助并送医，橘猫已康复', NOW(), NOW()),
('宠物粮食搬运志愿活动',              'transport', '搬运500公斤宠物粮食入库，志愿者们齐心协力完成任务',                                                     NULL, 0, NULL, 6,  '上海市闵行区莘庄镇申北路200号',      DATE_SUB(CURDATE(), INTERVAL 3 DAY),  DATE_SUB(CURDATE(), INTERVAL 3 DAY),  DATE_SUB(NOW(), INTERVAL 8 DAY), 1, 27, 25, 4, 4, '021-64003456', 4.0, '任务圆满完成，共搬运粮食500kg', NOW(), NOW()),
('领养日活动支持',                    'event',     '协助举办周末领养日活动，当天共有5只宠物成功找到新家',                                                NULL, 0, NULL, 8,  '上海市浦东新区张江高科技园区',        DATE_SUB(CURDATE(), INTERVAL 7 DAY),  DATE_SUB(CURDATE(), INTERVAL 7 DAY),  DATE_SUB(NOW(), INTERVAL 14 DAY), 1, 28, 20, 6, 6, '021-58002345', 5.0, '活动圆满成功，现场气氛热烈', NOW(), NOW()),
('宠物体检日协助',                    'medical',   '协助宠物医院进行救助宠物全面体检，帮助引导和记录',                                                   NULL, 1, NULL, 31, '上海市浦东新区张江高科技园区',        DATE_SUB(CURDATE(), INTERVAL 4 DAY),  DATE_SUB(CURDATE(), INTERVAL 4 DAY),  DATE_SUB(NOW(), INTERVAL 9 DAY), 1, 29, 25, 3, 3, '021-58002345', 3.5, '共协助体检宠物12只', NOW(), NOW()),

-- 已取消任务（1条）
('紧急宠物转运（取消）',             'transport', '因救助站暂时满员，本次转运任务临时取消',                                                            NULL, 1, NULL, 5,  '上海市徐汇区',                       DATE_ADD(CURDATE(), INTERVAL 1 DAY),  NULL,  DATE_SUB(NOW(), INTERVAL 3 DAY), 1, NULL, 35, 2, 0, '021-64002345', 4.0, '任务取消，原因：救助站满员', NOW(), NOW());

-- ================================================
-- 11. 操作日志（25条，多角色多类型操作记录）
-- ================================================
INSERT INTO operation_log (user_id, username, operation, detail, ip_address, user_role, created_at) VALUES
-- 管理员操作（5条）
(1, '系统管理员', 'LOGIN',       '{"msg":"管理员登录成功","browser":"Chrome"}',                              '192.168.1.100', 'admin',     DATE_SUB(NOW(), INTERVAL 30 MINUTE)),
(1, '系统管理员', 'PET_MANAGE',  '{"action":"上架","pet_name":"橘子","pet_id":1}',                          '192.168.1.100', 'admin',     DATE_SUB(NOW(), INTERVAL 25 MINUTE)),
(1, '系统管理员', 'PET_MANAGE',  '{"action":"上架","pet_name":"煤球","pet_id":2}',                          '192.168.1.100', 'admin',     DATE_SUB(NOW(), INTERVAL 20 MINUTE)),
(1, '系统管理员', 'AUDIT',       '{"action":"审核通过","shelter_name":"浦东爱心宠物之家","shelter_id":2}',  '192.168.1.100', 'admin',     DATE_SUB(NOW(), INTERVAL 15 MINUTE)),
(1, '系统管理员', 'PET_MANAGE',  '{"action":"上架","pet_name":"旺财","pet_id":13}',                        '192.168.1.100', 'admin',     DATE_SUB(NOW(), INTERVAL 10 MINUTE)),

-- 机构管理员操作（6条）
(16, '浦东救助中心管理员', 'LOGIN',       '{"msg":"机构管理员登录成功"}',                                 '192.168.1.101', 'institution_admin', DATE_SUB(NOW(), INTERVAL 2 HOUR)),
(16, '浦东救助中心管理员', 'PET_ADD',     '{"pet_name":"橘子","breed":"中华田园猫","pet_id":1}',          '192.168.1.101', 'institution_admin', DATE_SUB(NOW(), INTERVAL 2 HOUR)),
(16, '浦东救助中心管理员', 'PET_ADD',     '{"pet_name":"旺财","breed":"金毛寻回犬","pet_id":13}',          '192.168.1.101', 'institution_admin', DATE_SUB(NOW(), INTERVAL 90 MINUTE)),
(17, '徐汇救助站管理员',   'LOGIN',       '{"msg":"机构管理员登录成功"}',                                 '192.168.1.102', 'institution_admin', DATE_SUB(NOW(), INTERVAL 3 HOUR)),
(17, '徐汇救助站管理员',   'PET_ADD',     '{"pet_name":"雪球","breed":"英国短毛猫","pet_id":5}',          '192.168.1.102', 'institution_admin', DATE_SUB(NOW(), INTERVAL 3 HOUR)),
(17, '徐汇救助站管理员',   'PET_STATUS',  '{"pet_id":5,"action":"更新状态"}',                             '192.168.1.102', 'institution_admin', DATE_SUB(NOW(), INTERVAL 2 HOUR)),

-- 领养人操作（5条）
(3,  '陈建国',  'LOGIN',           '{"msg":"领养人登录成功"}',                                         '114.96.168.1',  'adopter',    DATE_SUB(NOW(), INTERVAL 4 HOUR)),
(3,  '陈建国',  'APPLICATION',      '{"pet_id":1,"pet_name":"橘子","apply_id":1,"status":"待审核"}',     '114.96.168.1',  'adopter',    DATE_SUB(NOW(), INTERVAL 4 HOUR)),
(4,  '林晓敏',  'LOGIN',           '{"msg":"领养人登录成功"}',                                         '114.96.168.2',  'adopter',    DATE_SUB(NOW(), INTERVAL 5 HOUR)),
(4,  '林晓敏',  'APPLICATION',      '{"pet_id":13,"pet_name":"旺财","apply_id":2,"status":"待审核"}',    '114.96.168.2',  'adopter',    DATE_SUB(NOW(), INTERVAL 5 HOUR)),
(7,  '刘志强',  'APPLICATION',      '{"pet_id":4,"pet_name":"花花","apply_id":5,"status":"已通过"}',     '114.96.168.3',  'adopter',    DATE_SUB(NOW(), INTERVAL 6 HOUR)),

-- 志愿者操作（4条）
(26, '志愿者小王', 'LOGIN',         '{"msg":"志愿者登录成功"}',                                        '114.96.168.10', 'volunteer',  DATE_SUB(NOW(), INTERVAL 1 DAY)),
(26, '志愿者小王', 'TASK_CLAIM',    '{"task_id":1,"task_title":"受伤金毛术后照护"}',                     '114.96.168.10', 'volunteer',  DATE_SUB(NOW(), INTERVAL 1 DAY)),
(27, '志愿者小李', 'TASK_CLAIM',    '{"task_id":3,"task_title":"猫咪照护周末活动"}',                    '114.96.168.11', 'volunteer',  DATE_SUB(NOW(), INTERVAL 2 DAY)),
(28, '志愿者小张', 'TASK_COMPLETE', '{"task_id":8,"task_title":"宠物粮食搬运志愿活动","result":"完成"}', '114.96.168.12', 'volunteer',  DATE_SUB(NOW(), INTERVAL 3 DAY)),

-- 宠物医院操作（5条）
(31, '上海宠物医院',   'LOGIN',     '{"msg":"宠物医院登录成功"}',                                      '192.168.2.1',   'pet_hospital', DATE_SUB(NOW(), INTERVAL 8 HOUR)),
(31, '上海宠物医院',   'RECORD_ADD','{"pet_id":1,"record_type":"vaccine","title":"猫三联疫苗第三针"}',   '192.168.2.1',   'pet_hospital', DATE_SUB(NOW(), INTERVAL 8 HOUR)),
(32, '浦东宠物诊所',   'LOGIN',     '{"msg":"宠物医院登录成功"}',                                      '192.168.2.2',   'pet_hospital', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(32, '浦东宠物诊所',   'RECORD_ADD','{"pet_id":2,"record_type":"vaccine","title":"狂犬疫苗接种"}',      '192.168.2.2',   'pet_hospital', DATE_SUB(NOW(), INTERVAL 1 DAY)),
(31, '上海宠物医院',   'RECORD_ADD','{"pet_id":13,"record_type":"checkup","title":"运动功能评估"}',     '192.168.2.1',   'pet_hospital', DATE_SUB(NOW(), INTERVAL 12 HOUR));

-- ================================================
-- 12. 数据统计确认
-- ================================================
SELECT '========== 测试数据加载完成 ==========' AS message;
SELECT CONCAT('用户总数: ',            COUNT(*)) AS stat FROM user;
SELECT CONCAT('领养申请总数: ',        COUNT(*)) AS stat FROM adoption_application;
SELECT CONCAT('宠物总数: ',            COUNT(*)) AS stat FROM pet WHERE deleted = 0;
SELECT CONCAT('救助站总数: ',          COUNT(*)) AS stat FROM shelter;
SELECT CONCAT('志愿者任务总数: ',      COUNT(*)) AS stat FROM volunteer_task WHERE deleted = 0;
SELECT CONCAT('健康档案总数: ',        COUNT(*)) AS stat FROM health_record WHERE deleted = 0;
SELECT CONCAT('回访记录总数: ',        COUNT(*)) AS stat FROM follow_up_record;
SELECT CONCAT('操作日志总数: ',        COUNT(*)) AS stat FROM operation_log;
SELECT CONCAT('领养人扩展信息: ',      COUNT(*)) AS stat FROM adopter;
SELECT CONCAT('志愿者扩展信息: ',      COUNT(*)) AS stat FROM volunteer;
SELECT CONCAT('宠物医院扩展信息: ',    COUNT(*)) AS stat FROM hospital;

-- ================================================
-- 测试账号汇总
-- ================================================
SELECT '========== 测试账号汇总 ==========' AS message
UNION ALL SELECT '【管理员】账号: 13800000000 / 123456（所有功能权限）'
UNION ALL SELECT '【领养人】账号: 13900000001~13 / 123456（共13名领养人）'
UNION ALL SELECT '【机构管理员】账号: 13700000001~10 / 123456（共10名，各区救助站）'
UNION ALL SELECT '【志愿者】账号: 13600000001~05 / 123456（共5名志愿者）'
UNION ALL SELECT '【宠物医院】账号: 13500000001~04 / 123456（共4家宠物医院）';
