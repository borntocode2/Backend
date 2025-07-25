package goodspace.backend.dto;

import goodspace.backend.domain.BaseEntity;
import goodspace.backend.domain.OrderStatus;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
public class OrderResponseDto extends BaseEntity {
    private Long userId;
    private List<OrderCartItemDto> orderCartItemDtos;
    private Long amount;
    private OrderStatus orderStatus;
    private String orderId;
    private String orderDate;
}
