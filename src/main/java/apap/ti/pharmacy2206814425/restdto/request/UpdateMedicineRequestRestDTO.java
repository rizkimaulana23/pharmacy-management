package apap.ti.pharmacy2206814425.restdto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateMedicineRequestRestDTO extends CreateMedicineRequestRestDTO {
    private String id;
}
