package apap.ti.pharmacy2206814425.restdto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreatePrescriptionRequestRestDTO {
    private String createdBy;
    private CreatePatientRequestRestDTO patientDTO;
    private List<MedicineQuantityRequestRestDTO> listMedicineQuantity;
}
