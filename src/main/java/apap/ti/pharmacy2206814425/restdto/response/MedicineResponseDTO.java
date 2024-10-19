package apap.ti.pharmacy2206814425.restdto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MedicineResponseDTO {
    private String id;
    private String name;
    private Long price;
    private Integer stock;
    private String description;
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMMM d, yyyy HH:mm:ss", timezone = "Asia/Jakarta")
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "Asia/Jakarta")
    private Date createdDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "Asia/Jakarta")
    private Date updatedDate;
    private String createdBy;
    private String updatedBy;
}
