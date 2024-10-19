package apap.ti.pharmacy2206814425;


import apap.ti.pharmacy2206814425.dto.CreateMedicineQuantityDTO;
import apap.ti.pharmacy2206814425.dto.CreatePrescriptionDTO;
import apap.ti.pharmacy2206814425.dto.ProcessPrescriptionDTO;
import apap.ti.pharmacy2206814425.model.Medicine;
import apap.ti.pharmacy2206814425.model.Prescription;
import apap.ti.pharmacy2206814425.repository.PatientDb;
import apap.ti.pharmacy2206814425.service.MedicineQuantityService;
import apap.ti.pharmacy2206814425.service.MedicineService;
import apap.ti.pharmacy2206814425.service.PrescriptionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@SpringBootTest
public class PrescriptionChangeStatusTest {

    @Autowired
    PrescriptionService prescriptionService;

    @Autowired
    PatientDb patientDb;

    @Autowired
    MedicineService medicineService;

    @Autowired
    MedicineQuantityService medicineQuantityService;

    @BeforeEach
    public void resetData(){
        patientDb.deleteAll();
    }

    @Test
    public void CurrentStockMoreThanNeededQuantity() {
        List<CreateMedicineQuantityDTO> mqList = new ArrayList<>();
        CreateMedicineQuantityDTO mq1 = new CreateMedicineQuantityDTO();
        mq1.setMedicineId("MED001");
        mq1.setQty(100);
        mqList.add(mq1);
        CreateMedicineQuantityDTO mq2 = new CreateMedicineQuantityDTO();
        mq2.setMedicineId("MED002");
        mq2.setQty(50);
        mqList.add(mq2);
        CreateMedicineQuantityDTO mq3 = new CreateMedicineQuantityDTO();
        mq3.setMedicineId("MED001");
        mq3.setQty(100);
        mqList.add(mq3);

        CreatePrescriptionDTO dto = new CreatePrescriptionDTO();
        dto.setNik("123");
        dto.setName("Rizki Maulana");
        dto.setEmail("rizki@gmail.com");
        dto.setGender(1);
        dto.setBirthDate(new Date());
        dto.setListMedicineQuantity(mqList);
        dto.setPharmacistId(UUID.fromString("52c1abd3-2421-4678-86b8-bd10ee8f8f40"));

        Prescription newPrescription = prescriptionService.createPrescription(dto);

        ProcessPrescriptionDTO processDTO = new ProcessPrescriptionDTO();
        List<Medicine> listMedicine = new ArrayList<>();

        medicineQuantityService.getMedicineQuantityForPrescription(newPrescription.getId()).forEach(prescription -> {
            listMedicine.add(medicineService.getMedicine(prescription.getMedicineId()));
        });
        processDTO.setListMedicine(listMedicine);
        processDTO.setProcessedBy("52c1abd3-2421-4678-86b8-bd10ee8f8f40");

        Prescription changedPrescription = prescriptionService.processPrescription(processDTO, newPrescription.getId());

        Assertions.assertEquals(2, changedPrescription.getStatus());
        Assertions.assertEquals(800, medicineService.getMedicine("MED001").getStock());
        Assertions.assertEquals(950, medicineService.getMedicine("MED002").getStock());
    }

    @Test
    public void currentStockLessThanQuantity() {
        List<CreateMedicineQuantityDTO> mqList = new ArrayList<>();
        CreateMedicineQuantityDTO mq1 = new CreateMedicineQuantityDTO();
        mq1.setMedicineId("MED001");
        mq1.setQty(10000);
        mqList.add(mq1);

        CreatePrescriptionDTO dto = new CreatePrescriptionDTO();
        dto.setNik("123");
        dto.setName("Rizki Maulana");
        dto.setEmail("rizki@gmail.com");
        dto.setGender(1);
        dto.setBirthDate(new Date());
        dto.setListMedicineQuantity(mqList);
        dto.setPharmacistId(UUID.fromString("52c1abd3-2421-4678-86b8-bd10ee8f8f40"));

        Prescription newPrescription = prescriptionService.createPrescription(dto);

        ProcessPrescriptionDTO processDTO = new ProcessPrescriptionDTO();
        List<Medicine> listMedicine = new ArrayList<>();

        medicineQuantityService.getMedicineQuantityForPrescription(newPrescription.getId()).forEach(prescription -> {
            listMedicine.add(medicineService.getMedicine(prescription.getMedicineId()));
        });
        processDTO.setListMedicine(listMedicine);
        processDTO.setProcessedBy("52c1abd3-2421-4678-86b8-bd10ee8f8f40");

        Prescription changedPrescription = prescriptionService.processPrescription(processDTO, newPrescription.getId());

        Assertions.assertEquals(1, changedPrescription.getStatus());
        Assertions.assertEquals(0, medicineService.getMedicine("MED001").getStock());
    }
}
