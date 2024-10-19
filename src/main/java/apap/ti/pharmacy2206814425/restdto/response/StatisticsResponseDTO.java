package apap.ti.pharmacy2206814425.restdto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StatisticsResponseDTO {
    private List<String> labels;
    private List<DatasetDTO> datasets;
}
