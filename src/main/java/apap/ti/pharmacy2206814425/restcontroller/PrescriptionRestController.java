package apap.ti.pharmacy2206814425.restcontroller;

import apap.ti.pharmacy2206814425.restdto.request.CreatePrescriptionRequestRestDTO;
import apap.ti.pharmacy2206814425.restdto.request.FindPatientRequestRestDTO;
import apap.ti.pharmacy2206814425.restdto.response.*;
import apap.ti.pharmacy2206814425.restservice.PrescriptionRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/prescription")
public class
PrescriptionRestController {

    @Autowired
    PrescriptionRestService prescriptionRestService;

    @GetMapping("")
    public ResponseEntity<?> viewAll(@RequestParam(value = "status", required = false) Integer status) {
        try {
            var baseResponse = new BaseResponseDTO<List<PrescriptionResponseDTO>>();
            var response = prescriptionRestService.getAllPrescriptions(status);
            baseResponse.setMessage("Berhasil mendapatkan daftar Prescription");
            baseResponse.setResult(response);
            return new ResponseEntity<>(baseResponse, HttpStatus.OK);
        } catch (Exception e) {
            var failedResponse = new BaseResponseDTO<>();
            failedResponse.setMessage(e.getMessage());
            return new ResponseEntity<>(failedResponse, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> view(@PathVariable String id) {
        try {
            var baseResponse = new BaseResponseDTO<PrescriptionResponseDTO>();
            var response = prescriptionRestService.getPrescription(id);
            baseResponse.setMessage("Showing prescription details");
            baseResponse.setResult(response);
            return new ResponseEntity<>(baseResponse, HttpStatus.OK);
        } catch (Exception e) {
            var failedResponse = new FailedResponseDTO();
            failedResponse.setError(e.getMessage());
            return new ResponseEntity<>(failedResponse, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/add/find-patient")
    public ResponseEntity<?> findPatient(@RequestBody FindPatientRequestRestDTO dto) {
        try {
            var baseResponse = new BaseResponseDTO<PatientResponseDTO>();
            var response = prescriptionRestService.findPatient(dto);
            baseResponse.setMessage("Patient berhasil ditemukan");
            baseResponse.setResult(response);
            return new ResponseEntity<>(baseResponse, HttpStatus.OK);
        } catch (Exception e) {
            var failedResponse = new BaseResponseDTO<>();
            failedResponse.setMessage(e.getMessage());
            return new ResponseEntity<>(failedResponse, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addForUnregisteredPatient(@RequestBody CreatePrescriptionRequestRestDTO dto) {
        try{
            var baseResponse = new BaseResponseDTO<PrescriptionResponseDTO>();
            PrescriptionResponseDTO response = prescriptionRestService.createPrescriptionUnregistered(dto);
            baseResponse.setMessage("Prescription berhasil dibuat");
            baseResponse.setResult(response);
            return new ResponseEntity<>(baseResponse, HttpStatus.OK);
        } catch (Exception e) {
            var failedResponse = new BaseResponseDTO<>();
            failedResponse.setMessage(e.getMessage());
            return new ResponseEntity<>(failedResponse, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/add/{patientId}")
    public ResponseEntity<?> addForRegisteredPatient(@PathVariable("patientId") UUID id, @RequestBody CreatePrescriptionRequestRestDTO dto) {
        try{
            var baseResponse = new BaseResponseDTO<PrescriptionResponseDTO>();
            PrescriptionResponseDTO response = prescriptionRestService.createPrescriptionRegistered(dto);
            baseResponse.setMessage("Prescription berhasil dibuat");
            baseResponse.setResult(response);
            return new ResponseEntity<>(baseResponse, HttpStatus.OK);
        } catch (Exception e) {
            var failedResponse = new BaseResponseDTO<>();
            failedResponse.setMessage(e.getMessage());
            return new ResponseEntity<>(failedResponse, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{id}/mark-done")
    public ResponseEntity<?> changePrescriptionStatus(@PathVariable("id") String id) {
        try {
            var baseResponse = new BaseResponseDTO<PrescriptionResponseDTO>();
            var response = prescriptionRestService.changePrescriptionStatus(id);
            baseResponse.setMessage("Status berhasil diubah");
            baseResponse.setResult(response);
            return new ResponseEntity<>(baseResponse, HttpStatus.OK);
        } catch (Exception e) {
            var failedResponse = new BaseResponseDTO<>();
            failedResponse.setMessage(e.getMessage());
            return new ResponseEntity<>(failedResponse ,HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> cancelPrescription(@PathVariable("id") String id) {
        try {
            var baseResponse = new BaseResponseDTO<PrescriptionResponseDTO>();
            var response = prescriptionRestService.cancelPrescription(id);
            baseResponse.setMessage("Status berhasil diubah");
            baseResponse.setResult(response);
            return new ResponseEntity<>(baseResponse, HttpStatus.OK);
        } catch (Exception e) {
            var failedResponse = new BaseResponseDTO<>();
            failedResponse.setMessage(e.getMessage());
            return new ResponseEntity<>(failedResponse, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/statistic")
    public ResponseEntity<?> showStatistics(@RequestParam(value = "month", required = false) String month,
                                            @RequestParam(value = "year", required = false) Integer year) {
        try {
            var baseResponse = new BaseResponseDTO<StatisticsResponseDTO>();
            var response = prescriptionRestService.getStatistics(month, year);
            baseResponse.setMessage("Statistics berhasil didapatkan");
            baseResponse.setResult(response);
            return new ResponseEntity<>(baseResponse, HttpStatus.OK);
        } catch (Exception e) {
            var failedResponse = new BaseResponseDTO<>();
            failedResponse.setMessage(e.getMessage());
            return new ResponseEntity<>(failedResponse, HttpStatus.BAD_REQUEST);
        }
    }
}
