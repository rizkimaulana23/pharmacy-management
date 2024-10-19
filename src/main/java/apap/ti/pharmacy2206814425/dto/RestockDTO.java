package apap.ti.pharmacy2206814425.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RestockDTO {
    @NotNull(message = "Medicine harus dipilih!")
    private String medicineId;

    @NotNull(message = "Quantity dari Medicine harus ada!")
    @Positive(message = "Quantity dari Medicine minimal 1!")
    private Integer qty;
}
