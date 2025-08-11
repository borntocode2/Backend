package goodspace.backend.admin.dto.order;

import lombok.Builder;

@Builder
public record TrackingNumberRegisterRequestDto(
        Long orderId,
        String trackingNumber
) {
}
