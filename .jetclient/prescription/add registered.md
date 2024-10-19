```toml
name = 'add registered'
method = 'POST'
url = '{{prescription.url}}/add/9198030f-a543-4408-90c8-fde0d1fac12d'
sortWeight = 3000000
id = 'a82e17e8-7348-43ed-a2ae-2b1596183e1b'

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
