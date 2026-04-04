import urllib.request, json
url = 'http://localhost:8081/api/pet/list?page=1&pageSize=100'
with urllib.request.urlopen(url, timeout=10) as resp:
    data = json.loads(resp.read())
    total = data['data']['total']
    pets = data['data']['pets']
    cats = sum(1 for p in pets if p.get('categoryType') == 'CATS')
    dogs = sum(1 for p in pets if p.get('categoryType') == 'DOGS')
    small = sum(1 for p in pets if p.get('categoryType') == 'SMALL_ANIMALS')
    print(f'API total={total}, page1={len(pets)}')
    print(f'Dogs={dogs} | Cats={cats} | Small={small}')
    print('Page 1 pets:')
    for p in pets[:12]:
        print(f'  [{p["id"]:2d}] {p["name"]:8s} | {p["categoryType"]} | {p["breed"]}')
    if len(pets) > 12:
        print(f'  ... ({len(pets)-12} more on page 1 due to pagination)')
