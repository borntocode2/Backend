package goodspace.backend.admin.dto.order;

import goodspace.backend.user.domain.DeliveryInfo;
import lombok.Builder;

@Builder
public record OrderUpdateRequestDto(
    Long orderId,
    PaymentApproveResultDto approveResult,
    DeliveryInfo deliveryInfo
) {
}
