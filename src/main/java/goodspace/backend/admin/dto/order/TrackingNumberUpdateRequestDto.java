package goodspace.backend.admin.dto.order;

import lombok.Builder;

@Builder
public record TrackingNumberUpdateRequestDto(
        Long orderId,
        String trackingNumber
) {
}
