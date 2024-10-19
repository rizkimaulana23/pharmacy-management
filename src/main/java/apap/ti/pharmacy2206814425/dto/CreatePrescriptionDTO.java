package apap.ti.pharmacy2206814425.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreatePrescriptionDTO {

    @NotBlank(message = "Nama Patient tidak boleh kosong")
    private String name;

    @NotBlank(message = "NIK Patient tidak boleh kosong")
    private String nik;

    @NotBlank(message = "Email Patient tidak boleh kosong")
    @Email
    private String email;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Birth Date Patient tidak boleh kosong")
    private Date birthDate;

    @Min(value = 0, message = "Nilai Gender Patient harus antara 0 atau 1")
    @Max(value = 1, message = "Nilai Gender Patient harus antara 0 atau 1")
    private int gender;

    @NotNull(message = "Pharmacist tidak boleh kosong")
    private UUID pharmacistId;

    @Valid
    @NotEmpty(message = "Medicine tidak boleh kosong")
    List<CreateMedicineQuantityDTO> listMedicineQuantity;
}
