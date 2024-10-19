package apap.ti.pharmacy2206814425.restdto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MedicineQuantityResponseDTO {
    private UUID id;
    private Integer quantity;
    private Integer fulfilledQty;
    private MedicineResponseDTO medicine;
}
