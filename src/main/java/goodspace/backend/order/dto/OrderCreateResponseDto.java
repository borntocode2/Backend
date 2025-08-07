package goodspace.backend.order.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class OrderCreateResponseDto {
    Long orderId;
}
