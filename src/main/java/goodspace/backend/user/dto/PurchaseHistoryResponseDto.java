package goodspace.backend.user.dto;

import goodspace.backend.global.parser.DateTimeParsers;
import goodspace.backend.order.domain.Order;
import goodspace.backend.order.domain.OrderCartItem;
import goodspace.backend.order.domain.OrderStatus;
import goodspace.backend.order.domain.PaymentApproveResult;
import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.List;

@Builder
public record PurchaseHistoryResponseDto(
    OffsetDateTime date,
    Long id,
    String itemInfo,
    Integer totalQuantity,
    Integer amount,
    OrderStatus status
) {
    public static PurchaseHistoryResponseDto from(Order order) {
        PaymentApproveResult approveResult = order.getApproveResult();

        return PurchaseHistoryResponseDto.builder()
                .date(DateTimeParsers.parseOffsetDateTime(approveResult.getPaidAt()))
                .id(order.getId())
                .itemInfo(approveResult.getGoodsName())
                .totalQuantity(getTotalQuantity(order.getOrderCartItems()))
                .amount(approveResult.getAmount())
                .status(order.getOrderStatus())
                .build();
    }

    private static int getTotalQuantity(List<OrderCartItem> orderCartItems) {
        return orderCartItems.stream()
                .mapToInt(OrderCartItem::getQuantity)
                .sum();
    }
}
