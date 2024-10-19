package apap.ti.pharmacy2206814425.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateMedicineQuantityDTO {
    @NotBlank(message = "Medicine tidak boleh kosong")
    private String medicineId;

    @NotNull(message = "Quantity tidak boleh kosong")
    @Positive(message = "Quantity haruslah bernilai positif")
    private int qty;
}
