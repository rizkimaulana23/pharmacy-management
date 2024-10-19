package apap.ti.pharmacy2206814425.restcontroller;

import apap.ti.pharmacy2206814425.restdto.request.CreateMedicineRequestRestDTO;
import apap.ti.pharmacy2206814425.restdto.request.MultipleRestockMedicineRestDTO;
import apap.ti.pharmacy2206814425.restdto.request.RestockMedicineRestDTO;
import apap.ti.pharmacy2206814425.restdto.request.UpdateMedicineRequestRestDTO;
import apap.ti.pharmacy2206814425.restdto.response.BaseResponseDTO;
import apap.ti.pharmacy2206814425.restdto.response.FailedResponseDTO;
import apap.ti.pharmacy2206814425.restdto.response.MedicineResponseDTO;
import apap.ti.pharmacy2206814425.restservice.MedicineRestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicine")
public class MedicineRestController {

    @Autowired
    private MedicineRestService medicineRestService;

    @GetMapping("")
    public ResponseEntity<?> viewAll() {
        try {
            var successResponse = new BaseResponseDTO<List<MedicineResponseDTO>>();

            List<MedicineResponseDTO> response = medicineRestService.getAll();
            successResponse.setResult(response);
            successResponse.setMessage("Daftar Medicine berhasil ditemukan");
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        } catch (Exception e) {
            var failedResponse = new FailedResponseDTO();
            failedResponse.setError(e.getMessage());
            return new ResponseEntity<>(failedResponse, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> viewDetail(@PathVariable("id") String id) {
        try {
            var successResponse = new BaseResponseDTO<MedicineResponseDTO>();

            MedicineResponseDTO response = medicineRestService.viewMedicne(id);
            successResponse.setResult(response);
            successResponse.setMessage("Daftar Medicine berhasil ditemukan");
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        } catch (Exception e) {
            var failedResponse = new FailedResponseDTO();
            failedResponse.setError(e.getMessage());
            return new ResponseEntity<>(failedResponse, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody CreateMedicineRequestRestDTO medicineDTO) {
        try {
            var baseResponse = new BaseResponseDTO<MedicineResponseDTO>();

            var response = medicineRestService.addMedicine(medicineDTO);

            baseResponse.setMessage("Berhasil menambahkan Medicine baru");
            baseResponse.setResult(response);
            return new ResponseEntity<>(baseResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            var failedResponse = new FailedResponseDTO();
            failedResponse.setError(e.getMessage());
            return new ResponseEntity<>(failedResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody UpdateMedicineRequestRestDTO medicineDTO) {
        try {
            var baseResponse = new BaseResponseDTO<MedicineResponseDTO>();

            var response = medicineRestService.updateMedicine(medicineDTO);
            baseResponse.setMessage("Berhasil memperbarui Medicine");
            baseResponse.setResult(response);
            return new ResponseEntity<>(baseResponse, HttpStatus.OK);
        } catch (Exception e) {
            var failedResponse = new FailedResponseDTO();
            failedResponse.setError(e.getMessage());
            return new ResponseEntity<>(failedResponse, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/restock")
    public ResponseEntity<?> multipleRestock(@RequestBody MultipleRestockMedicineRestDTO restockDTO) {
        try {
            var baseResponse = new BaseResponseDTO<List<MedicineResponseDTO>>();

            List<MedicineResponseDTO> response = medicineRestService.multipleRestock(restockDTO);
            baseResponse.setResult(response);
            baseResponse.setMessage("Daftar Medicine berhasil di-restock");
            return new ResponseEntity<>(baseResponse, HttpStatus.OK);
        } catch (Exception e) {
            var failedResponse = new FailedResponseDTO();
            failedResponse.setError(e.getMessage());
            return new ResponseEntity<>(failedResponse, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{id}/update-stock")
    public ResponseEntity<?> restock(@PathVariable("id") String id, @RequestBody RestockMedicineRestDTO restockDTO) {
        try {
            var baseResponse = new BaseResponseDTO<MedicineResponseDTO>();

            MedicineResponseDTO response = medicineRestService.restock(restockDTO);
            baseResponse.setMessage("Berhasil menambah stock Medicine");
            baseResponse.setResult(response);
            return new ResponseEntity<>(baseResponse, HttpStatus.OK);
        } catch (Exception e) {
            var failedResponse = new FailedResponseDTO();
            failedResponse.setError(e.getMessage());
            return new ResponseEntity<>(failedResponse, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> delete(@PathVariable("id") String id) {
        try {
            var successResponse = new BaseResponseDTO<MedicineResponseDTO>();
            medicineRestService.delete(id);
            successResponse.setMessage(String.format("Medicine dengan ID %s berhasil dihapus", id));
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        } catch (Exception e) {
            var failedResponse = new FailedResponseDTO();
            failedResponse.setError(e.getMessage());
            return new ResponseEntity<>(failedResponse, HttpStatus.NOT_FOUND);
        }
    }
}
