```toml
name = 'add'
method = 'POST'
url = '{{medicine.url}}/add'
sortWeight = 1000000
id = '5fb3f902-9376-4f15-8aba-414f4b5843d9'

[body]
type = 'JSON'
raw = '''
{
  "name": "SANMOL",
  "price": 10000,
  "stock": 5,
  "description": "Obat Batuk",
  "createdBy": "Pharmacist 1"
}'''
```
