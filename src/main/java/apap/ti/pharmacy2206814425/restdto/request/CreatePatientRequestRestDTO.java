package apap.ti.pharmacy2206814425.restdto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreatePatientRequestRestDTO {
    private String nik;
    private String name;
    private String email;
    private Integer gender;
    private Date birthDate;
    private String createdBy;
}
