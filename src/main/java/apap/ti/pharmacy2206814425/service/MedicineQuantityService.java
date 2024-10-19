package apap.ti.pharmacy2206814425.service;

import apap.ti.pharmacy2206814425.model.MedicineQuantity;

import java.util.List;
import java.util.UUID;

public interface MedicineQuantityService {
    void deleteMedicineQuantityFromUpdate(UUID id);
    MedicineQuantity newMedicineQuantity(String prescriptionId);
    List<MedicineQuantity> getMedicineQuantityForPrescription(String id);
}
