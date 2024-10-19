package apap.ti.pharmacy2206814425.dto;

import apap.ti.pharmacy2206814425.model.Medicine;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProcessPrescriptionDTO {
    @NotBlank(message = "Pharmacist tidak boleh kosong")
    private String processedBy;

    private List<Medicine> listMedicine;
}
