package apap.ti.pharmacy2206814425.restdto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class HomeResponseDTO {
    private int countPrescription;
    private int countMedicine;
    private int countPatient;
}
