package goodspace.backend.admin.dto.order;

import goodspace.backend.order.domain.OrderStatus;

public record OrderStatusChangeRequestDto(
        Long orderId,
        OrderStatus status
) {
}
