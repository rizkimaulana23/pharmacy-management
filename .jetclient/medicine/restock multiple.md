```toml
name = 'restock multiple'
method = 'POST'
url = '{{medicine.url}}/restock'
sortWeight = 5000000
id = 'c4b365ae-9b69-4154-944f-65d3b2a75be9'

[body]
type = 'JSON'
raw = '''
{
  "listRestockDTO" : [
    {
      "id": "e2b75a45-f755-40c8-8127-de8898edf4a7",
      "stock": 0
    }
  ]
}'''
```
