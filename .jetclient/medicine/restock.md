```toml
name = 'restock'
method = 'POST'
url = '{{medicine.url}}/MED001/update-stock'
sortWeight = 6000000
id = '60a37c37-4455-4a3d-8187-320e458d9b8f'

[body]
type = 'JSON'
raw = '''
{
  "id" : "MED001",
  "stock": 100
}'''
```
