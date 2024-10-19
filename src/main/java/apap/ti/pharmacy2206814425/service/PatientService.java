package apap.ti.pharmacy2206814425.service;

import apap.ti.pharmacy2206814425.model.Patient;

import java.util.UUID;

public interface PatientService {
    Patient findPatient(UUID id);
    Patient findPatientByNik(String nik);
}
