-- ================================================
-- 救助站测试数据（28条，覆盖上海全部16区）
-- 2026-04-02 更新版
-- 使用方式：直接在 MySQL 中执行这些 INSERT 语句
-- 注意：如提示主键冲突（ID 已存在），先 DELETE 再重新插入，或改用 REPLACE
-- ================================================

-- 清理旧数据（可选，如需重新导入）
-- DELETE FROM shelter WHERE id >= 1;

-- ================================================
-- 1. 浦东新区（3个，ID 1-3）
-- ================================================
INSERT INTO shelter (id, name, address, lat, lon, max_capacity, current_capacity, region_code, phone, status, medical_level, description, entry_status, audit_status, created_time, updated_time) VALUES
(1, '浦东动物救助中心',    '上海市浦东新区浦东大道1234号',   31.2247, 121.5437, 200, 85,  '310115', '021-58001234', 0, 3, '上海市最大的动物救助中心，拥有专业医疗团队和大型救助基地',               1, 1, NOW(), NOW())
ON DUPLICATE KEY UPDATE name=VALUES(name), address=VALUES(address), lat=VALUES(lat), lon=VALUES(lon), max_capacity=VALUES(max_capacity), current_capacity=VALUES(current_capacity), description=VALUES(description);

INSERT INTO shelter (id, name, address, lat, lon, max_capacity, current_capacity, region_code, phone, status, medical_level, description, entry_status, audit_status, created_time, updated_time) VALUES
(2, '浦东爱心宠物之家',    '上海市浦东新区张江高科技园区',  31.2054, 121.5689, 100, 45,  '310115', '021-58002345', 0, 2, '专注流浪猫救助，拥有完善的领养服务体系',                                       1, 1, NOW(), NOW())
ON DUPLICATE KEY UPDATE name=VALUES(name), address=VALUES(address), lat=VALUES(lat), lon=VALUES(lon), max_capacity=VALUES(max_capacity), current_capacity=VALUES(current_capacity), description=VALUES(description);

INSERT INTO shelter (id, name, address, lat, lon, max_capacity, current_capacity, region_code, phone, status, medical_level, description, entry_status, audit_status, created_time, updated_time) VALUES
(3, '川沙流浪动物收容所',  '上海市浦东新区川沙镇新川路',    31.1919, 121.6716, 150, 120, '310115', '021-58003456', 1, 1, '政府合作的流浪动物收容中心',                                                   1, 1, NOW(), NOW())
ON DUPLICATE KEY UPDATE name=VALUES(name), address=VALUES(address), lat=VALUES(lat), lon=VALUES(lon), max_capacity=VALUES(max_capacity), current_capacity=VALUES(current_capacity), description=VALUES(description);

-- ================================================
-- 2. 徐汇区（2个，ID 4-5）
-- ================================================
INSERT INTO shelter (id, name, address, lat, lon, max_capacity, current_capacity, region_code, phone, status, medical_level, description, entry_status, audit_status, created_time, updated_time) VALUES
(4, '徐汇区宠物救助站',    '上海市徐汇区漕河泾开发区',      31.1691, 121.4230, 120, 60,  '310104', '021-64001234', 0, 2, '位于市中心的专业宠物救助机构',                                                 1, 1, NOW(), NOW())
ON DUPLICATE KEY UPDATE name=VALUES(name), address=VALUES(address), lat=VALUES(lat), lon=VALUES(lon), max_capacity=VALUES(max_capacity), current_capacity=VALUES(current_capacity), description=VALUES(description);

INSERT INTO shelter (id, name, address, lat, lon, max_capacity, current_capacity, region_code, phone, status, medical_level, description, entry_status, audit_status, created_time, updated_time) VALUES
(5, '上海小动物保护协会',  '上海市徐汇区衡山路516号',        31.2089, 121.4469, 80,  35,  '310104', '021-64002345', 0, 1, '民间动物保护组织，专注流浪猫狗救助',                                           1, 1, NOW(), NOW())
ON DUPLICATE KEY UPDATE name=VALUES(name), address=VALUES(address), lat=VALUES(lat), lon=VALUES(lon), max_capacity=VALUES(max_capacity), current_capacity=VALUES(current_capacity), description=VALUES(description);

-- ================================================
-- 3. 闵行区（2个，ID 6-7）
-- ================================================
INSERT INTO shelter (id, name, address, lat, lon, max_capacity, current_capacity, region_code, phone, status, medical_level, description, entry_status, audit_status, created_time, updated_time) VALUES
(6, '闵行区流浪动物救助中心','上海市闵行区莘庄镇申北路200号',31.1056, 121.3776, 180, 90,  '310112', '021-64003456', 0, 2, '闵行区官方救助中心，设施完善',                                                 1, 1, NOW(), NOW())
ON DUPLICATE KEY UPDATE name=VALUES(name), address=VALUES(address), lat=VALUES(lat), lon=VALUES(lon), max_capacity=VALUES(max_capacity), current_capacity=VALUES(current_capacity), description=VALUES(description);

INSERT INTO shelter (id, name, address, lat, lon, max_capacity, current_capacity, region_code, phone, status, medical_level, description, entry_status, audit_status, created_time, updated_time) VALUES
(7, '闵行爱心动物之家',    '上海市闵行区七宝镇沪松公路',    31.1524, 121.3411, 60,  25,  '310112', '021-64004567', 0, 1, '社区型动物救助站，服务周边居民',                                               1, 1, NOW(), NOW())
ON DUPLICATE KEY UPDATE name=VALUES(name), address=VALUES(address), lat=VALUES(lat), lon=VALUES(lon), max_capacity=VALUES(max_capacity), current_capacity=VALUES(current_capacity), description=VALUES(description);

-- ================================================
-- 4. 静安区（2个，ID 8-9）
-- ================================================
INSERT INTO shelter (id, name, address, lat, lon, max_capacity, current_capacity, region_code, phone, status, medical_level, description, entry_status, audit_status, created_time, updated_time) VALUES
(8, '静安区宠物服务中心',  '上海市静安区北京西路1068号',    31.2325, 121.4544, 80,  40,  '310106', '021-63101234', 0, 2, '位于市中心的综合性宠物服务机构',                                              1, 1, NOW(), NOW())
ON DUPLICATE KEY UPDATE name=VALUES(name), address=VALUES(address), lat=VALUES(lat), lon=VALUES(lon), max_capacity=VALUES(max_capacity), current_capacity=VALUES(current_capacity), description=VALUES(description);

INSERT INTO shelter (id, name, address, lat, lon, max_capacity, current_capacity, region_code, phone, status, medical_level, description, entry_status, audit_status, created_time, updated_time) VALUES
(9, '上海流浪动物临时收容站','上海市静安区共和新路2800号',  31.2567, 121.4634, 100, 55,  '310106', '021-63102345', 0, 1, '紧急救助临时收容点',                                                           1, 1, NOW(), NOW())
ON DUPLICATE KEY UPDATE name=VALUES(name), address=VALUES(address), lat=VALUES(lat), lon=VALUES(lon), max_capacity=VALUES(max_capacity), current_capacity=VALUES(current_capacity), description=VALUES(description);

-- ================================================
-- 5. 长宁区（2个，ID 10-11）
-- ================================================
INSERT INTO shelter (id, name, address, lat, lon, max_capacity, current_capacity, region_code, phone, status, medical_level, description, entry_status, audit_status, created_time, updated_time) VALUES
(10, '长宁区动物救助站',   '上海市长宁区虹桥路2285号',      31.2018, 121.3929, 90,  42,  '310105', '021-62101234', 0, 2, '长宁区专业动物救助机构',                                                       1, 1, NOW(), NOW())
ON DUPLICATE KEY UPDATE name=VALUES(name), address=VALUES(address), lat=VALUES(lat), lon=VALUES(lon), max_capacity=VALUES(max_capacity), current_capacity=VALUES(current_capacity), description=VALUES(description);

INSERT INTO shelter (id, name, address, lat, lon, max_capacity, current_capacity, region_code, phone, status, medical_level, description, entry_status, audit_status, created_time, updated_time) VALUES
(11, '上海宠物爱心联盟',   '上海市长宁区中山公园附近',      31.2241, 121.4178, 70,  30,  '310105', '021-62102345', 0, 1, '志愿者运营的爱心救助组织',                                                     1, 1, NOW(), NOW())
ON DUPLICATE KEY UPDATE name=VALUES(name), address=VALUES(address), lat=VALUES(lat), lon=VALUES(lon), max_capacity=VALUES(max_capacity), current_capacity=VALUES(current_capacity), description=VALUES(description);

-- ================================================
-- 6. 黄浦区（1个，ID 12）
-- ================================================
INSERT INTO shelter (id, name, address, lat, lon, max_capacity, current_capacity, region_code, phone, status, medical_level, description, entry_status, audit_status, created_time, updated_time) VALUES
(12, '黄浦区宠物之家',    '上海市黄浦区南京东路步行街',    31.2317, 121.4832, 50,  22,  '310101', '021-63103456', 0, 1, '市中心小型救助站',                                                             1, 1, NOW(), NOW())
ON DUPLICATE KEY UPDATE name=VALUES(name), address=VALUES(address), lat=VALUES(lat), lon=VALUES(lon), max_capacity=VALUES(max_capacity), current_capacity=VALUES(current_capacity), description=VALUES(description);

-- ================================================
-- 7. 杨浦区（1个，ID 13）
-- ================================================
INSERT INTO shelter (id, name, address, lat, lon, max_capacity, current_capacity, region_code, phone, status, medical_level, description, entry_status, audit_status, created_time, updated_time) VALUES
(13, '杨浦区流浪动物保护中心','上海市杨浦区五角场镇国定路',  31.3031, 121.5189, 110, 50,  '310110', '021-65101234', 0, 2, '杨浦区官方动物保护中心',                                                       1, 1, NOW(), NOW())
ON DUPLICATE KEY UPDATE name=VALUES(name), address=VALUES(address), lat=VALUES(lat), lon=VALUES(lon), max_capacity=VALUES(max_capacity), current_capacity=VALUES(current_capacity), description=VALUES(description);

-- ================================================
-- 8. 普陀区（1个，ID 14）
-- ================================================
INSERT INTO shelter (id, name, address, lat, lon, max_capacity, current_capacity, region_code, phone, status, medical_level, description, entry_status, audit_status, created_time, updated_time) VALUES
(14, '普陀区宠物救助站',   '上海市普陀区曹杨路200号',       31.2428, 121.3912, 85,  38,  '310107', '021-62801234', 0, 1, '普陀区爱心救助站',                                                              1, 1, NOW(), NOW())
ON DUPLICATE KEY UPDATE name=VALUES(name), address=VALUES(address), lat=VALUES(lat), lon=VALUES(lon), max_capacity=VALUES(max_capacity), current_capacity=VALUES(current_capacity), description=VALUES(description);

-- ================================================
-- 9. 虹口区（1个，ID 15）
-- ================================================
INSERT INTO shelter (id, name, address, lat, lon, max_capacity, current_capacity, region_code, phone, status, medical_level, description, entry_status, audit_status, created_time, updated_time) VALUES
(15, '虹口区小动物救助中心', '上海市虹口区四川北路1500号',   31.2642, 121.4923, 75,  32,  '310109', '021-63201234', 0, 1, '虹口区志愿者救助组织',                                                          1, 1, NOW(), NOW())
ON DUPLICATE KEY UPDATE name=VALUES(name), address=VALUES(address), lat=VALUES(lat), lon=VALUES(lon), max_capacity=VALUES(max_capacity), current_capacity=VALUES(current_capacity), description=VALUES(description);

-- ================================================
-- 10. 宝山区（1个，ID 16）
-- ================================================
INSERT INTO shelter (id, name, address, lat, lon, max_capacity, current_capacity, region_code, phone, status, medical_level, description, entry_status, audit_status, created_time, updated_time) VALUES
(16, '宝山区流浪动物收容所', '上海市宝山区顾村镇潘广路',    31.4028, 121.3542, 130, 65,  '310113', '021-56101234', 0, 2, '宝山区大型动物收容中心',                                                         1, 1, NOW(), NOW())
ON DUPLICATE KEY UPDATE name=VALUES(name), address=VALUES(address), lat=VALUES(lat), lon=VALUES(lon), max_capacity=VALUES(max_capacity), current_capacity=VALUES(current_capacity), description=VALUES(description);

-- ================================================
-- 11. 嘉定区（1个，ID 17）
-- ================================================
INSERT INTO shelter (id, name, address, lat, lon, max_capacity, current_capacity, region_code, phone, status, medical_level, description, entry_status, audit_status, created_time, updated_time) VALUES
(17, '嘉定区宠物救助中心',  '上海市嘉定区安亭镇曹安公路',   31.3742, 121.1689, 95,  45,  '310114', '021-59501234', 0, 1, '嘉定区动物救助站',                                                              1, 1, NOW(), NOW())
ON DUPLICATE KEY UPDATE name=VALUES(name), address=VALUES(address), lat=VALUES(lat), lon=VALUES(lon), max_capacity=VALUES(max_capacity), current_capacity=VALUES(current_capacity), description=VALUES(description);

-- ================================================
-- 12. 松江区（1个，ID 18）
-- ================================================
INSERT INTO shelter (id, name, address, lat, lon, max_capacity, current_capacity, region_code, phone, status, medical_level, description, entry_status, audit_status, created_time, updated_time) VALUES
(18, '松江区流浪动物之家',  '上海市松江区大学城文汇路',     31.0317, 121.2289, 100, 48,  '310117', '021-57701234', 0, 1, '松江区爱心救助站',                                                              1, 1, NOW(), NOW())
ON DUPLICATE KEY UPDATE name=VALUES(name), address=VALUES(address), lat=VALUES(lat), lon=VALUES(lon), max_capacity=VALUES(max_capacity), current_capacity=VALUES(current_capacity), description=VALUES(description);

-- ================================================
-- 13. 青浦区（1个，ID 19）
-- ================================================
INSERT INTO shelter (id, name, address, lat, lon, max_capacity, current_capacity, region_code, phone, status, medical_level, description, entry_status, audit_status, created_time, updated_time) VALUES
(19, '青浦区动物救助站',    '上海市青浦区徐泾镇沪青平公路', 31.1592, 121.3089, 110, 52,  '310118', '021-69201234', 0, 2, '青浦区综合救助中心',                                                           1, 1, NOW(), NOW())
ON DUPLICATE KEY UPDATE name=VALUES(name), address=VALUES(address), lat=VALUES(lat), lon=VALUES(lon), max_capacity=VALUES(max_capacity), current_capacity=VALUES(current_capacity), description=VALUES(description);

-- ================================================
-- 14. 奉贤区（1个，ID 20）
-- ================================================
INSERT INTO shelter (id, name, address, lat, lon, max_capacity, current_capacity, region_code, phone, status, medical_level, description, entry_status, audit_status, created_time, updated_time) VALUES
(20, '奉贤区流浪动物收容中心','上海市奉贤区南桥镇解放路',  30.9189, 121.4589, 120, 58,  '310120', '021-57401234', 0, 1, '奉贤区动物收容站',                                                              1, 1, NOW(), NOW())
ON DUPLICATE KEY UPDATE name=VALUES(name), address=VALUES(address), lat=VALUES(lat), lon=VALUES(lon), max_capacity=VALUES(max_capacity), current_capacity=VALUES(current_capacity), description=VALUES(description);

-- ================================================
-- 15. 新增救助站（2026-04-02 扩充，ID 21-28）
-- ================================================

-- 金山区（新增第1个，填补空白）
INSERT INTO shelter (id, name, address, lat, lon, max_capacity, current_capacity, region_code, phone, status, medical_level, description, entry_status, audit_status, created_time, updated_time) VALUES
(21, '金山区流浪动物救助站', '上海市金山区石化卫清西路188号', 30.7412, 121.3312, 80,   35,  '310116', '021-57901234', 0, 1, '金山区专业动物救助站，设施完善，服务周边居民',                                  1, 1, NOW(), NOW())
ON DUPLICATE KEY UPDATE name=VALUES(name), address=VALUES(address), lat=VALUES(lat), lon=VALUES(lon), max_capacity=VALUES(max_capacity), current_capacity=VALUES(current_capacity), description=VALUES(description);

-- 崇明区（新增第1个，填补空白）
INSERT INTO shelter (id, name, address, lat, lon, max_capacity, current_capacity, region_code, phone, status, medical_level, description, entry_status, audit_status, created_time, updated_time) VALUES
(22, '崇明区动物保护中心',  '上海市崇明区城桥镇人民路88号', 31.6234, 121.4056, 90,   40,  '310151', '021-59601234', 0, 1, '崇明区官方动物保护中心，生态救助特色',                                         1, 1, NOW(), NOW())
ON DUPLICATE KEY UPDATE name=VALUES(name), address=VALUES(address), lat=VALUES(lat), lon=VALUES(lon), max_capacity=VALUES(max_capacity), current_capacity=VALUES(current_capacity), description=VALUES(description);

-- 浦东新增（临港新城）
INSERT INTO shelter (id, name, address, lat, lon, max_capacity, current_capacity, region_code, phone, status, medical_level, description, entry_status, audit_status, created_time, updated_time) VALUES
(23, '临港新城宠物之家',   '上海市浦东新区临港新城环湖西三路', 30.9189, 121.9078, 60, 20, '310115', '021-58004567', 0, 2, '临港新城社区宠物救助站，志愿者运营',                                             1, 1, NOW(), NOW())
ON DUPLICATE KEY UPDATE name=VALUES(name), address=VALUES(address), lat=VALUES(lat), lon=VALUES(lon), max_capacity=VALUES(max_capacity), current_capacity=VALUES(current_capacity), description=VALUES(description);

-- 闵行新增（虹桥商务区）
INSERT INTO shelter (id, name, address, lat, lon, max_capacity, current_capacity, region_code, phone, status, medical_level, description, entry_status, audit_status, created_time, updated_time) VALUES
(24, '虹桥商务区宠物中心', '上海市闵行区申长路288号',       31.1901, 121.3156, 70,   28,  '310112', '021-64005678', 0, 2, '虹桥商务区专业宠物服务中心，交通便利',                                         1, 1, NOW(), NOW())
ON DUPLICATE KEY UPDATE name=VALUES(name), address=VALUES(address), lat=VALUES(lat), lon=VALUES(lon), max_capacity=VALUES(max_capacity), current_capacity=VALUES(current_capacity), description=VALUES(description);

-- 虹口新增（北外滩）
INSERT INTO shelter (id, name, address, lat, lon, max_capacity, current_capacity, region_code, phone, status, medical_level, description, entry_status, audit_status, created_time, updated_time) VALUES
(25, '北外滩动物救助站',   '上海市虹口区东大名路500号',     31.2734, 121.5023, 55,   22,  '310109', '021-63205678', 0, 1, '北外滩社区动物救助站，服务航运社区居民',                                       1, 1, NOW(), NOW())
ON DUPLICATE KEY UPDATE name=VALUES(name), address=VALUES(address), lat=VALUES(lat), lon=VALUES(lon), max_capacity=VALUES(max_capacity), current_capacity=VALUES(current_capacity), description=VALUES(description);

-- 静安新增（大宁地区）
INSERT INTO shelter (id, name, address, lat, lon, max_capacity, current_capacity, region_code, phone, status, medical_level, description, entry_status, audit_status, created_time, updated_time) VALUES
(26, '大宁地区宠物收容所', '上海市静安区共和新路3800号',   31.2856, 121.4456, 50,   18,  '310106', '021-63105678', 0, 1, '大宁地区社区宠物收容站，专注社区流浪动物救助',                                   1, 1, NOW(), NOW())
ON DUPLICATE KEY UPDATE name=VALUES(name), address=VALUES(address), lat=VALUES(lat), lon=VALUES(lon), max_capacity=VALUES(max_capacity), current_capacity=VALUES(current_capacity), description=VALUES(description);

-- 浦东新增（张江高科技，医疗等级最高）
INSERT INTO shelter (id, name, address, lat, lon, max_capacity, current_capacity, region_code, phone, status, medical_level, description, entry_status, audit_status, created_time, updated_time) VALUES
(27, '张江高科技宠物站',   '上海市浦东新区张江高科技园区碧波路', 31.2101, 121.6078, 65, 25, '310115', '021-58006789', 0, 3, '张江高科技园区专业宠物医疗站，拥有先进医疗设备',                                 1, 1, NOW(), NOW())
ON DUPLICATE KEY UPDATE name=VALUES(name), address=VALUES(address), lat=VALUES(lat), lon=VALUES(lon), max_capacity=VALUES(max_capacity), current_capacity=VALUES(current_capacity), description=VALUES(description);

-- 浦东新增（南汇新城）
INSERT INTO shelter (id, name, address, lat, lon, max_capacity, current_capacity, region_code, phone, status, medical_level, description, entry_status, audit_status, created_time, updated_time) VALUES
(28, '南汇新城动物之家',   '上海市浦东新区南汇新城镇申港大道', 30.8823, 121.8578, 75, 30, '310115', '021-58007890', 0, 1, '南汇新城社区动物之家，志愿者活跃',                                                 1, 1, NOW(), NOW())
ON DUPLICATE KEY UPDATE name=VALUES(name), address=VALUES(address), lat=VALUES(lat), lon=VALUES(lon), max_capacity=VALUES(max_capacity), current_capacity=VALUES(current_capacity), description=VALUES(description);

-- ================================================
-- 验证查询
-- ================================================
SELECT '=== 救助站总数 ===' AS info, COUNT(*) AS count FROM shelter WHERE deleted = 0 OR deleted IS NULL;
SELECT id, name, region_code, lat, lon, max_capacity, current_capacity, medical_level, status, entry_status, audit_status FROM shelter ORDER BY id;
