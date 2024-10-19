package apap.ti.pharmacy2206814425.service;

import apap.ti.pharmacy2206814425.model.MedicineQuantity;
import apap.ti.pharmacy2206814425.repository.MedicineQuantityDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MedicineQuantityServiceImpl implements MedicineQuantityService {

    @Autowired
    MedicineQuantityDb medicineQuantityDb;

    @Autowired
    PrescriptionService prescriptionService;

    @Override
    public void deleteMedicineQuantityFromUpdate(UUID id) {
        if (medicineQuantityDb.findById(id).isPresent()) {
            medicineQuantityDb.deleteById(id);
        }
    }

    @Override
    public MedicineQuantity newMedicineQuantity(String prescriptionId) {
        return null;
    }

    @Override
    public List<MedicineQuantity> getMedicineQuantityForPrescription(String id) {
        return medicineQuantityDb.findAllByPrescriptionId(id);
    }
}
