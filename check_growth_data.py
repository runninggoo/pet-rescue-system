import pymysql
import os

conn = pymysql.connect(
    host='localhost',
    port=3306,
    user=os.environ.get('DB_USERNAME', 'root'),
    password=os.environ.get('DB_PASSWORD', ''),
    database='pet_rescue',
    charset='utf8mb4'
)
cursor = conn.cursor()

# 1. 检查哪些表存在
print("=== 数据库中的表 ===")
cursor.execute("SHOW TABLES")
tables = [t[0] for t in cursor.fetchall()]
for t in tables:
    print(f"  - {t}")

# 2. 检查 volunteer_profile 表
if 'volunteer_profile' in tables:
    print("\n=== volunteer_profile 表 ===")
    cursor.execute("SELECT * FROM volunteer_profile WHERE deleted=0")
    rows = cursor.fetchall()
    print(f"共 {len(rows)} 条记录")
    for r in rows:
        print(r)
else:
    print("\n=== volunteer_profile 表不存在 ===")

# 3. 检查 point_history 表
if 'point_history' in tables:
    print("\n=== point_history 表 ===")
    cursor.execute("SELECT * FROM point_history ORDER BY created_at DESC LIMIT 20")
    rows = cursor.fetchall()
    print(f"共 {len(rows)} 条记录 (显示前20条)")
    for r in rows:
        print(r)
else:
    print("\n=== point_history 表不存在 ===")

# 4. 检查 level_rule 表
if 'level_rule' in tables:
    print("\n=== level_rule 表 ===")
    cursor.execute("SELECT * FROM level_rule ORDER BY level")
    rows = cursor.fetchall()
    print(f"共 {len(rows)} 条记录")
    for r in rows:
        print(r)
else:
    print("\n=== level_rule 表不存在 ===")

# 5. 查看志愿者用户
print("\n=== 志愿者用户 ===")
cursor.execute("SELECT id, name, phone, role FROM user WHERE role='VOLUNTEER' AND deleted=0")
rows = cursor.fetchall()
if rows:
    print(f"共 {len(rows)} 条志愿者:")
    for r in rows:
        print(r)
else:
    print("无数据")

conn.close()
