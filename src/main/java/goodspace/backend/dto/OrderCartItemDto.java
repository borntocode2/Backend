package goodspace.backend.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderCartItemDto {
    private Long itemId;
    private Integer quantity;
    private Integer amount;
    private Long orderId;
}
