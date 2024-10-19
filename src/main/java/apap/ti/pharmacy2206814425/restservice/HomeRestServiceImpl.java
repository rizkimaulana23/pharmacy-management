package apap.ti.pharmacy2206814425.restservice;

import apap.ti.pharmacy2206814425.repository.MedicineDb;
import apap.ti.pharmacy2206814425.repository.PatientDb;
import apap.ti.pharmacy2206814425.repository.PrescriptionDb;
import apap.ti.pharmacy2206814425.restdto.response.HomeResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HomeRestServiceImpl implements HomeRestService {

    @Autowired
    PrescriptionDb prescriptionDb;

    @Autowired
    PatientDb patientDb;

    @Autowired
    MedicineDb medicineDb;

    @Override
    public HomeResponseDTO getHome() {
        int countPrescription = prescriptionDb.findAllByStatusOrStatus(0,1).size();
        int countPatient = patientDb.findAll().size();
        int countMedicine = medicineDb.findAll().size();

        HomeResponseDTO home = new HomeResponseDTO();
        home.setCountPrescription(countPrescription);
        home.setCountPatient(countPatient);
        home.setCountMedicine(countMedicine);
        return home;
    }
}
