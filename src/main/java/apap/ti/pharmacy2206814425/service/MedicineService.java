package apap.ti.pharmacy2206814425.service;

import apap.ti.pharmacy2206814425.dto.CreateMedicineDTO;
import apap.ti.pharmacy2206814425.dto.RestockDTO;
import apap.ti.pharmacy2206814425.dto.RestockMultipleDTO;
import apap.ti.pharmacy2206814425.dto.UpdateMedicineDTO;
import apap.ti.pharmacy2206814425.model.Medicine;

import java.util.List;

public interface MedicineService {
    Medicine addMedicine(CreateMedicineDTO medicineDTO);
    List<Medicine> getAllMedicine();
    Medicine getMedicine(String id);
    Medicine updateMedicine(UpdateMedicineDTO medicineDTO);
    void restockMedicine(RestockMultipleDTO restockDTO);
    void restockSingleMedicine(RestockDTO restockDTO);
    boolean deleteMedicine(String id);
}
