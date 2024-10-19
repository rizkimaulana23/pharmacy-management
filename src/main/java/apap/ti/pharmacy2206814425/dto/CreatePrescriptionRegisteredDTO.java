package apap.ti.pharmacy2206814425.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreatePrescriptionRegisteredDTO {
    @NotNull(message = "Pharmacist tidak boleh kosong")
    private UUID pharmacistId;

    @Valid
    @NotEmpty(message = "Medicine tidak boleh kosong")
    List<CreateMedicineQuantityDTO> listMedicineQuantity;
}
