package apap.ti.pharmacy2206814425.service;

import apap.ti.pharmacy2206814425.dto.CreateMedicineDTO;
import apap.ti.pharmacy2206814425.dto.RestockDTO;
import apap.ti.pharmacy2206814425.dto.RestockMultipleDTO;
import apap.ti.pharmacy2206814425.dto.UpdateMedicineDTO;
import apap.ti.pharmacy2206814425.model.Medicine;
import apap.ti.pharmacy2206814425.model.MedicineQuantity;
import apap.ti.pharmacy2206814425.model.Prescription;
import apap.ti.pharmacy2206814425.repository.MedicineDb;
import apap.ti.pharmacy2206814425.repository.PrescriptionDb;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Date;
import java.util.Formatter;
import java.util.List;

@Service
@Slf4j
public class MedicineServiceImpl implements MedicineService {

    @Autowired
    private MedicineDb medicineDb;

    @Autowired
    private PrescriptionService prescriptionService;

    @Autowired
    private PrescriptionDb prescriptionDb;

    @Autowired
    private MedicineQuantityService medicineQuantityService;

    private final WebClient webClient;

    public MedicineServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("https://localhost:8080/api")
                .build();
    }

    @Override
    public List<Medicine> getAllMedicine() {
        return medicineDb.findAllByDeletedDateIsNull();
    }

    @Override
    public Medicine addMedicine(CreateMedicineDTO medicineDTO) {
        Medicine newMedicine = new Medicine();
        newMedicine.setId(formatMedicineId(medicineDb.count() + 1));
        newMedicine.setName(medicineDTO.getName());
        newMedicine.setPrice(medicineDTO.getPrice());
        newMedicine.setDescription(medicineDTO.getDescription());
        newMedicine.setStock(medicineDTO.getStock());
        newMedicine.setDescription(medicineDTO.getDescription());
        newMedicine.setCreatedBy(medicineDTO.getCreatedBy());
        newMedicine.setUpdatedBy(medicineDTO.getCreatedBy());

        return medicineDb.save(newMedicine);
    }

    @Override
    public Medicine getMedicine(String id) {
        return medicineDb.findById(id).isEmpty() ? null : medicineDb.findById(id).get();
    }

    @Override
    public Medicine updateMedicine(UpdateMedicineDTO medicineDTO) {
        log.info("HEYYY " + medicineDTO.getId());
        Medicine medicine = medicineDb.findMedicineById(medicineDTO.getId());
        medicine.setName(medicineDTO.getName());
        medicine.setPrice(medicineDTO.getPrice());
        medicine.setDescription(medicineDTO.getDescription());
        medicine.setUpdatedBy(medicineDTO.getUpdatedBy());
        return medicineDb.save(medicine);
    }

    @Override
    public void restockMedicine(RestockMultipleDTO restockDTO) {
        for (RestockDTO restock : restockDTO.getListRestockDTO()) {
            Medicine medicine = medicineDb.findMedicineById(restock.getMedicineId());
            medicine.setStock(medicine.getStock() + restock.getQty());
            medicineDb.save(medicine);
        }
    }

    @Override
    public void restockSingleMedicine(RestockDTO restockDTO) {
        Medicine medicine = medicineDb.findMedicineById(restockDTO.getMedicineId());
        medicine.setStock(medicine.getStock() + restockDTO.getQty());
        medicineDb.save(medicine);
    }

    @Override
    public boolean deleteMedicine(String id) {
        Medicine medicine = medicineDb.findMedicineById(id);
        if (medicine == null) return false;

        List<Prescription> listPrescription = prescriptionDb.findAllByStatusOrStatus(0, 1);
        for (Prescription prescription : listPrescription) {
            List<MedicineQuantity> listMq = medicineQuantityService.getMedicineQuantityForPrescription(prescription.getId());
            for (MedicineQuantity mq: listMq) {
                if (mq.getMedicineId().equals(id)) return false;
            }
        }

        medicine.setDeletedDate(new Date());
        medicineDb.save(medicine);
        return true;
    }

    private static String formatMedicineId(long number) {
        Formatter formatter = new Formatter();
        formatter.format("%03d", number);
        String formattedString = formatter.toString();
        formatter.close();
        return "MED" + formattedString;
    }
}
