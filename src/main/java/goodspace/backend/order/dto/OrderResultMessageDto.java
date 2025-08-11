package goodspace.backend.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderResultMessageDto {
    private String message;
    private String status;
}
