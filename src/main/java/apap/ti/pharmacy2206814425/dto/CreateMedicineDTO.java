package apap.ti.pharmacy2206814425.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateMedicineDTO {
    @NotBlank(message = "Nama Medicine tidak boleh kosong")
    private String name;
    @NotNull(message = "Stock Medicine tidak boleh kosong")
    @Positive(message = "Stock Medicine tidak boleh bernilai negatif")
    private Integer stock;
    private String description;
    @NotNull(message = "Price Medicine tidak boleh kosong")
    @Min(value = 1000, message = "Price Medicine minimal 1000")
    private Long price;
    @NotBlank(message = "Created By Medicine tidak boleh kosong")
    private String createdBy;
}
