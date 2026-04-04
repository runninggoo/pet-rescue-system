import urllib.request, json

def test_login(phone, password, label):
    data = json.dumps({'phone': phone, 'password': password}).encode()
    req = urllib.request.Request('http://localhost:8081/api/auth/login', data=data, headers={'Content-Type': 'application/json'})
    try:
        r = urllib.request.urlopen(req, timeout=5)
        resp = json.loads(r.read())
        token = resp.get('data', {}).get('token', '')
        print(f'{label}: login OK, token={token[:30]}...' if token else f'{label}: no token')
        # Test /list with token
        req2 = urllib.request.Request('http://localhost:8081/api/volunteer-task/list?page=1&pageSize=10', headers={'Authorization': f'Bearer {token}'})
        r2 = urllib.request.urlopen(req2, timeout=5)
        resp2 = json.loads(r2.read())
        tasks = resp2.get('data', {}).get('tasks', [])
        print(f'  /list -> {len(tasks)} tasks, code={resp2.get("code")}')
    except Exception as e:
        print(f'{label}: ERROR - {e}')

test_login('13800000000', '123456', 'Admin')
test_login('13600000001', 'password', 'Volunteer')