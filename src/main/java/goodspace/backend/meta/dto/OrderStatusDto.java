package goodspace.backend.meta.dto;

import goodspace.backend.order.domain.OrderStatus;
import lombok.Builder;

@Builder
public record OrderStatusDto(
        String name,
        String korean
) {
    public static OrderStatusDto from(OrderStatus orderStatus) {
        return OrderStatusDto.builder()
                .name(orderStatus.name())
                .korean(orderStatus.getKorean())
                .build();
    }
}
