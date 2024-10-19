package apap.ti.pharmacy2206814425.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdatePrescriptionDTO {
    private String prescriptionId;
    private String patientName;
    private UUID updatedBy;
    private List<UpdateMedicineQuantityDTO> listMedicineQuantity;
}
