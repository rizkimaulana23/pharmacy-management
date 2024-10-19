package apap.ti.pharmacy2206814425.service;

import apap.ti.pharmacy2206814425.dto.CreatePrescriptionDTO;
import apap.ti.pharmacy2206814425.dto.CreatePrescriptionRegisteredDTO;
import apap.ti.pharmacy2206814425.dto.ProcessPrescriptionDTO;
import apap.ti.pharmacy2206814425.dto.UpdatePrescriptionDTO;
import apap.ti.pharmacy2206814425.model.Prescription;

import java.util.List;
import java.util.UUID;

public interface PrescriptionService {
    List<Prescription> getAllPrescription();
    Prescription getPrescription(String id);
    Prescription createPrescription(CreatePrescriptionDTO dto);
    Prescription createPrescription(UUID id, CreatePrescriptionRegisteredDTO dto);
    Prescription updatePrescription(UpdatePrescriptionDTO dto, String id);
    Prescription processPrescription(ProcessPrescriptionDTO dto, String id);
    List<Prescription> getAllPrescriptionWithStatus(Integer status);
    boolean deletePrescription(String id);
    String generatePrescriptionId(CreatePrescriptionDTO dto);
}
