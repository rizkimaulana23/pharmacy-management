package apap.ti.pharmacy2206814425.restdto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PrescriptionResponseDTO {
    private String id;
    private Long totalPrice;
    private String status;
    private String createdDate;
    private String updatedDate;
    private PharmacistResponseDTO pharmacist;
    private PatientResponseDTO patient;
    private List<MedicineQuantityResponseDTO> listMedicineQuantity;
}
