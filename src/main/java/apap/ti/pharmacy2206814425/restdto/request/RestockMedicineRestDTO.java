package apap.ti.pharmacy2206814425.restdto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RestockMedicineRestDTO {
    @NotNull
    private String id;
    @Min(value = 1)
    private Integer stock;
}
