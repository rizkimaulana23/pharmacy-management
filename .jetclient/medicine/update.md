```toml
name = 'update'
method = 'PUT'
url = '{{medicine.url}}/update'
sortWeight = 4000000
id = '03403f12-03ad-4d0c-bcb4-a3a68ec36184'

[body]
type = 'JSON'
raw = '''
{
  "id": "e2b75a45-f755-40c8-8127-de8898edf4a7",
  "name": "BODREX AHAHAHAH",
  "price": 10000,
  "stock": 10,
  "description": "Obat Batuk",
  "createdBy": "Rizki Maulana"
}'''
```
