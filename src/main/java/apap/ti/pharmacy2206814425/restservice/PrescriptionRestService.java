package apap.ti.pharmacy2206814425.restservice;

import apap.ti.pharmacy2206814425.restdto.request.CreatePrescriptionRequestRestDTO;
import apap.ti.pharmacy2206814425.restdto.request.FindPatientRequestRestDTO;
import apap.ti.pharmacy2206814425.restdto.response.PatientResponseDTO;
import apap.ti.pharmacy2206814425.restdto.response.PrescriptionResponseDTO;
import apap.ti.pharmacy2206814425.restdto.response.StatisticsResponseDTO;

import java.util.List;

public interface PrescriptionRestService {
    List<PrescriptionResponseDTO> getAllPrescriptions(Integer status);
    PrescriptionResponseDTO getPrescription(String id) throws  Exception;
    PatientResponseDTO findPatient(FindPatientRequestRestDTO dto) throws Exception;
    PrescriptionResponseDTO createPrescriptionUnregistered(CreatePrescriptionRequestRestDTO dto) throws Exception;
    PrescriptionResponseDTO createPrescriptionRegistered(CreatePrescriptionRequestRestDTO dto) throws Exception;
    PrescriptionResponseDTO changePrescriptionStatus(String id) throws Exception;
    PrescriptionResponseDTO cancelPrescription(String id) throws Exception;
    StatisticsResponseDTO getStatistics(String month, Integer year) throws Exception;
}
