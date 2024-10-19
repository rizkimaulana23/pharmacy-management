package apap.ti.pharmacy2206814425.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RestockMultipleDTO {
    @Valid
    @NotEmpty(message = "Medicine untuk di-restock tidak boleh kosong")
    private List<RestockDTO> listRestockDTO;
}
