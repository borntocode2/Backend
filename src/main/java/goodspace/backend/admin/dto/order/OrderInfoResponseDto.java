package goodspace.backend.admin.dto.order;

import goodspace.backend.admin.dto.item.ItemInfoResponseDto;
import goodspace.backend.order.domain.Order;
import goodspace.backend.order.domain.OrderCartItem;
import goodspace.backend.order.domain.OrderStatus;
import goodspace.backend.order.domain.PaymentApproveResult;
import goodspace.backend.user.domain.DeliveryInfo;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record OrderInfoResponseDto(
        Long id,
        PaymentApproveResultResponseDto approveResult,
        DeliveryInfo deliveryInfo,
        OrderStatus status,
        LocalDateTime createAt,
        LocalDateTime updatedAt,
        List<ItemInfoResponseDto> items
) {
    public static OrderInfoResponseDto from(Order order) {
        List<ItemInfoResponseDto> items = order.getOrderCartItems().stream()
                .map(OrderCartItem::getItem)
                .map(ItemInfoResponseDto::from)
                .toList();
        PaymentApproveResult approveResult = order.getApproveResult();

        return OrderInfoResponseDto.builder()
                .id(order.getId())
                .approveResult(approveResult == null ? null : PaymentApproveResultResponseDto.from(approveResult))
                .deliveryInfo(order.getDeliveryInfo())
                .status(order.getOrderStatus())
                .createAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .items(items)
                .build();
    }
}
