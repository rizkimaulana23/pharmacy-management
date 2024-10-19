package apap.ti.pharmacy2206814425.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateMedicineQuantityDTO extends CreateMedicineQuantityDTO {
    private UUID id;

}
