package apap.ti.pharmacy2206814425.restdto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PharmacistResponseDTO {
    private UUID id;
    private String name;
    private String email;
    private Integer gender;
    private Date createdDate;
    private Date updatedDate;
    private String createdBy;
    private String updatedBy;
}
