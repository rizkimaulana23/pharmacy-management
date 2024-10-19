package apap.ti.pharmacy2206814425.restservice;

import apap.ti.pharmacy2206814425.restdto.request.CreateMedicineRequestRestDTO;
import apap.ti.pharmacy2206814425.restdto.request.MultipleRestockMedicineRestDTO;
import apap.ti.pharmacy2206814425.restdto.request.RestockMedicineRestDTO;
import apap.ti.pharmacy2206814425.restdto.request.UpdateMedicineRequestRestDTO;
import apap.ti.pharmacy2206814425.restdto.response.MedicineResponseDTO;

import java.util.List;

public interface MedicineRestService {
    MedicineResponseDTO addMedicine(CreateMedicineRequestRestDTO medicineDTO) throws Exception;
    List<MedicineResponseDTO> getAll() throws Exception;
    MedicineResponseDTO viewMedicne(String id) throws Exception;
    MedicineResponseDTO updateMedicine(UpdateMedicineRequestRestDTO medicineDTO) throws Exception;
    List<MedicineResponseDTO> multipleRestock(MultipleRestockMedicineRestDTO restockDTO) throws Exception;
    MedicineResponseDTO restock(RestockMedicineRestDTO restockDTO) throws Exception;
    void delete(String id) throws Exception;
}
