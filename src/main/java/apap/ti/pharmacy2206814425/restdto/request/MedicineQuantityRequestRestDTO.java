package apap.ti.pharmacy2206814425.restdto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MedicineQuantityRequestRestDTO {
    private String id;
    private int quantity;
}
