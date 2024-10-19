package apap.ti.pharmacy2206814425.restdto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BaseResponseDTO<T> {
    private String message;
    private T result;
}
