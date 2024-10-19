package apap.ti.pharmacy2206814425.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateMedicineDTO extends CreateMedicineDTO{
    private String id;
    @NotBlank(message = "Updated By Medicine tidak boleh kosong")
    private String updatedBy;
}
