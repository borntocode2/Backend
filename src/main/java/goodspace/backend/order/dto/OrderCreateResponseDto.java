package goodspace.backend.order.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderCreateResponseDto {
    Long orderId;
}
