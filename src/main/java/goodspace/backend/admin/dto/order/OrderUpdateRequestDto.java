package goodspace.backend.admin.dto.order;

import goodspace.backend.order.domain.PaymentApproveResult;
import goodspace.backend.user.domain.Delivery;
import lombok.Builder;

@Builder
public record OrderUpdateRequestDto(
    Long orderId,
    PaymentApproveResult approveResult,
    Delivery deliveryInfo
) {
}
