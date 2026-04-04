import pymysql
import os
from datetime import date, timedelta

conn = pymysql.connect(
    host='localhost',
    port=3306,
    user=os.environ.get('DB_USERNAME', 'root'),
    password=os.environ.get('DB_PASSWORD', ''),
    database='pet_rescue',
    charset='utf8mb4'
)
cursor = conn.cursor()

print("=== Setting up volunteer growth system ===")

# Read volunteer-growth.sql (relative to this script's location)
script_dir = os.path.dirname(os.path.abspath(__file__))
sql_path = os.path.join(script_dir, 'backend', 'src', 'main', 'resources', 'volunteer-growth.sql')
with open(sql_path, 'r', encoding='utf-8') as f:
    sql_content = f.read()

statements = []
current = ''
for line in sql_content.split('\n'):
    line = line.strip()
    if line.startswith('--') or not line:
        continue
    current += line + ' '
    if line.endswith(';'):
        statements.append(current.strip())
        current = ''

for stmt in statements:
    try:
        cursor.execute(stmt)
        conn.commit()
    except Exception as e:
        print(f"  [warn] {str(e)[:60]}")

print("Tables created!")

cursor.execute("SHOW TABLES")
tables = [t[0] for t in cursor.fetchall()]
for t in ['volunteer_profile', 'point_history', 'level_rule']:
    print(f"  {'[OK]' if t in tables else '[FAIL]'} {t}")

# === xiaowang data ===
print("\n=== Setting up xiaowang (phone=13600000001) ===")
cursor.execute("SELECT id FROM user WHERE phone='13600000001' AND deleted=0 LIMIT 1")
row = cursor.fetchone()
if not row:
    print("User not found")
else:
    uid = int(row[0])
    print(f"xiaowang user_id = {uid}")

    cursor.execute("DELETE FROM point_history WHERE user_id=" + str(uid))
    cursor.execute("DELETE FROM volunteer_profile WHERE user_id=" + str(uid))
    conn.commit()

    today = date.today()
    today_str = str(today)

    # Profile: 305 pts, Lv.3, 5 tasks, 7-day streak
    cursor.execute(f"""
        INSERT INTO volunteer_profile (user_id, total_points, available_points, current_level,
            current_level_name, total_tasks, total_hours, title, continuous_sign_days, last_sign_date)
        VALUES ({uid}, 305, 305, 3, '爱心使者', 5, 15.5, '资深铲屎官', 7, '{today_str}')
    """)
    conn.commit()
    print("  [OK] Profile: 305pts / Lv.3 / 5 tasks / 7-day streak")

    # 7-day sign-in
    balances = [5, 10, 15, 20, 25, 30, 55]
    for i in range(7):
        sign_date = today - timedelta(days=i)
        is_7th = (i == 6)
        points = 25 if is_7th else 5
        bal = balances[i]
        source = 'CONTINUOUS_SIGN_7' if is_7th else 'DAILY_SIGN'
        desc = '7-day streak reward' if is_7th else 'Daily sign'
        created = str(sign_date) + ' 08:30:00'
        cursor.execute(f"""
            INSERT INTO point_history (user_id, points, balance, source, description, created_at)
            VALUES ({uid}, {points}, {bal}, '{source}', '{desc}', '{created}')
        """)
    conn.commit()
    print("  [OK] 7-day sign-in history")

    # 5 task completion records
    task_data = [
        (20, 55, -20), (30, 85, -18), (35, 120, -12), (45, 165, -6), (40, 205, -3)
    ]
    task_desc = ['Daily cleaning task', 'Emergency rescue task', 'Vaccination support',
                 'Cross-zone transport', 'Adoption day event support']
    for j, (pts, bal, days) in enumerate(task_data):
        d = abs(days)
        created = str(today - timedelta(days=d)) + ' 14:00:00'
        cursor.execute(f"""
            INSERT INTO point_history (user_id, points, balance, source, description, created_at)
            VALUES ({uid}, {pts}, {bal}, 'TASK_COMPLETE', '{task_desc[j]}', '{created}')
        """)
    conn.commit()
    print("  [OK] 5 task completion records")

# === Other volunteers for leaderboard ===
print("\n=== Other volunteers ===")
cursor.execute("SELECT id FROM user WHERE role='volunteer' AND deleted=0 AND id!=" + str(uid) + " LIMIT 6")
others = cursor.fetchall()
leaderboard_data = [
    (50, 1, '新手上路'), (450, 3, '爱心使者'), (1800, 5, '金牌志愿者'),
    (2200, 6, '公益之星'), (680, 4, '救助达人'), (120, 2, '小小志愿者'),
]
for i, (vid,) in enumerate(others):
    if i >= len(leaderboard_data):
        break
    pts, lvl, lname = leaderboard_data[i]
    cursor.execute("DELETE FROM point_history WHERE user_id=" + str(vid))
    cursor.execute("DELETE FROM volunteer_profile WHERE user_id=" + str(vid))
    cursor.execute(f"""
        INSERT INTO volunteer_profile (user_id, total_points, available_points, current_level, current_level_name)
        VALUES ({vid}, {pts}, {pts}, {lvl}, '{lname}')
    """)
    cursor.execute(f"""
        INSERT INTO point_history (user_id, points, balance, source, description)
        VALUES ({vid}, {pts}, {pts}, 'TASK_COMPLETE', 'Task completion')
    """)
    conn.commit()
    print(f"  [OK] uid={vid}: {pts}pts Lv.{lvl}")

print("\n=== Verification ===")
cursor.execute("SELECT user_id, total_points, current_level, current_level_name, total_tasks FROM volunteer_profile ORDER BY total_points DESC")
for p in cursor.fetchall():
    print(f"  uid={p[0]}, {p[1]}pts Lv.{p[2]}/{p[3]}, {p[4]}tasks")

cursor.execute("SELECT COUNT(*) FROM point_history")
print(f"Point history: {cursor.fetchone()[0]} records")
cursor.execute("SELECT COUNT(*) FROM level_rule")
print(f"Level rules: {cursor.fetchone()[0]} records")

conn.close()
print("\nAll done!")
