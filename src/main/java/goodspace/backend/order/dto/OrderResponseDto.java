package goodspace.backend.order.dto;

import goodspace.backend.global.domain.BaseEntity;
import goodspace.backend.order.domain.OrderStatus;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
public class OrderResponseDto extends BaseEntity {
    private List<OrderCartItemDto> orderCartItemDtos;
    private Long amount;
    private OrderStatus orderStatus;
    private String orderId;
    private String orderDate;
}
