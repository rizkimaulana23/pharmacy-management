package apap.ti.pharmacy2206814425.restservice;

import apap.ti.pharmacy2206814425.model.Medicine;
import apap.ti.pharmacy2206814425.repository.MedicineDb;
import apap.ti.pharmacy2206814425.repository.PharmacistDb;
import apap.ti.pharmacy2206814425.restdto.request.CreateMedicineRequestRestDTO;
import apap.ti.pharmacy2206814425.restdto.request.MultipleRestockMedicineRestDTO;
import apap.ti.pharmacy2206814425.restdto.request.RestockMedicineRestDTO;
import apap.ti.pharmacy2206814425.restdto.request.UpdateMedicineRequestRestDTO;
import apap.ti.pharmacy2206814425.restdto.response.MedicineResponseDTO;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
@Slf4j
public class MedicineRestServiceImpl implements MedicineRestService {

    @Autowired
    private MedicineDb medicineDb;

    @Autowired
    private PharmacistDb pharmacistDb;

    @Override
    public MedicineResponseDTO addMedicine(CreateMedicineRequestRestDTO medicineDTO) throws Exception {
        if (pharmacistDb.findFirstByName(medicineDTO.getCreatedBy()) == null) throw new Exception("Pharmacist tidak ditemukan");
        if (medicineDb.findMedicineByName(medicineDTO.getName()) != null) throw new Exception(String.format("Nama obat %s sudah digunakan", medicineDTO.getName()));

        Medicine medicine = new Medicine();
        medicine.setId(formatMedicineId(medicineDb.count() + 1));
        medicine.setName(medicineDTO.getName());
        medicine.setDescription(medicineDTO.getDescription());
        medicine.setCreatedBy(medicineDTO.getCreatedBy());
        medicine.setUpdatedBy(medicineDTO.getCreatedBy());
        medicine.setStock(medicineDTO.getStock());
        medicine.setPrice(medicineDTO.getPrice());

        medicineDb.save(medicine);

        Medicine newMedicine = medicineDb.findMedicineById(medicine.getId());
        return medicineToMedicineResponseDTO(newMedicine);
    }

    @Override
    public List<MedicineResponseDTO> getAll() throws Exception {
        List<Medicine> medicines = medicineDb.findAllByDeletedDateIsNull();

        if (medicines.isEmpty()) {
            throw new Exception("Medicine kosong");
        }

        List<MedicineResponseDTO> medicineResponseDTOS = new ArrayList<>();

        for (Medicine medicine : medicines) {
            medicineResponseDTOS.add(medicineToMedicineResponseDTO(medicine));
        }

        return medicineResponseDTOS;
    }

    @Override
    public MedicineResponseDTO viewMedicne(String id) throws Exception {
        Medicine medicine = medicineDb.findMedicineById(id);
        if (medicine == null || medicine.getDeletedDate() != null) throw new Exception("Medicine tidak ditemukan");

        return medicineToMedicineResponseDTO(medicine);
    }

    @Override
    public MedicineResponseDTO updateMedicine(UpdateMedicineRequestRestDTO medicineDTO) throws Exception {
        Medicine medicine = medicineDb.findMedicineById(medicineDTO.getId());
        if (medicine == null || medicine.getDeletedDate() != null) throw new Exception("Medicine tidak ditemukan");
        medicine.setName(medicineDTO.getName());
        medicine.setDescription(medicineDTO.getDescription());
        medicine.setPrice(medicineDTO.getPrice());
        medicine.setUpdatedBy(medicineDTO.getCreatedBy());
        medicineDb.save(medicine);

        Medicine newMedicine = medicineDb.findMedicineById(medicine.getId());
        return medicineToMedicineResponseDTO(newMedicine);
    }

    @Override
    public List<MedicineResponseDTO> multipleRestock(MultipleRestockMedicineRestDTO restockDTO) throws Exception {
        List<String> ids = new ArrayList<>();
        for (RestockMedicineRestDTO dto: restockDTO.getListRestockDTO()) {
            Medicine medicine = medicineDb.findMedicineById(dto.getId());
            if (medicine == null || medicine.getDeletedDate() != null) throw new Exception("Medicine tidak ditemukan");
            medicine.setStock(medicine.getStock() + dto.getStock());
            medicineDb.save(medicine);
            ids.add(medicine.getId());
        }

        List<MedicineResponseDTO> response = new ArrayList<>();
        List<String> restockedMedicine = removeDuplicatesId(ids);
        for (String id: restockedMedicine) {
            Medicine medicine = medicineDb.findMedicineById(id);
            response.add(medicineToMedicineResponseDTO(medicine));
        }
        return response;
    }

    @Override
    public MedicineResponseDTO restock(RestockMedicineRestDTO dto) throws Exception {
        Medicine medicine = medicineDb.findMedicineById(dto.getId());
        if (medicine == null || medicine.getDeletedDate() != null) throw new Exception("Medicine tidak ditemukan");
        medicine.setStock(medicine.getStock() + dto.getStock());
        medicineDb.save(medicine);

        Medicine newMedicine = medicineDb.findMedicineById(dto.getId());
        return medicineToMedicineResponseDTO(newMedicine);
    }

    @Override
    public void delete(String id) throws Exception {
        Medicine medicine = medicineDb.findMedicineById(id);
        if (medicine == null || medicine.getDeletedDate() != null) throw new Exception("Medicine tidak ditemukan");
        medicine.setDeletedDate(new Date());
    }

    private MedicineResponseDTO medicineToMedicineResponseDTO (Medicine medicine) {
        MedicineResponseDTO medicineResponseDTO = new MedicineResponseDTO();
        medicineResponseDTO.setId(medicine.getId());
        medicineResponseDTO.setName(medicine.getName());
        medicineResponseDTO.setPrice(medicine.getPrice());
        medicineResponseDTO.setStock(medicine.getStock());
        medicineResponseDTO.setDescription(medicine.getDescription());
        medicineResponseDTO.setCreatedBy(medicine.getCreatedBy());
        medicineResponseDTO.setUpdatedBy(medicine.getCreatedBy());
        medicineResponseDTO.setUpdatedDate(medicine.getUpdatedDate());
        medicineResponseDTO.setCreatedDate(medicine.getCreatedDate());
        return medicineResponseDTO;
    }

    private List<String> removeDuplicatesId(List<String> list) {
        return new ArrayList<>(new HashSet<>(list));
    }

    private static String formatMedicineId(long number) {
        Formatter formatter = new Formatter();
        formatter.format("%03d", number);
        String formattedString = formatter.toString();
        formatter.close();
        return "MED" + formattedString;
    }
}
