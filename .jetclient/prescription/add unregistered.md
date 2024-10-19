```toml
name = 'add unregistered'
method = 'POST'
url = '{{prescription.url}}/add'
sortWeight = 2000000
id = 'd92638e1-87a0-4d74-81c3-b8d43cc6b962'

[body]
type = 'JSON'
raw = '''
{
  "createdBy": "Pharmacist 1",
  "patientDTO" : {
    "nik": "123",
    "name": "Rizki Maulana",
    "email": "rizki@example.com",
    "gender": 1,
    "birthDate": "2004-02-21",
    "createdBy": "Pharmacist 1"
  },
  listMedicineQuantity: [
    {
      "id": "MED001",
      "quantity": 10
    },
  ]
}'''
```
