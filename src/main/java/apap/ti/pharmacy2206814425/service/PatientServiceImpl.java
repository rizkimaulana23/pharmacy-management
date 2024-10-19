package apap.ti.pharmacy2206814425.service;

import apap.ti.pharmacy2206814425.model.Patient;
import apap.ti.pharmacy2206814425.repository.PatientDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientDb patientDb;

    @Override
    public Patient findPatient(UUID id) {
        return patientDb.findById(id).isPresent() ? patientDb.findById(id).get() : null;
    }

    @Override
    public Patient findPatientByNik(String nik) {
        return patientDb.findByNik(nik);
    }
}
