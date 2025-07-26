package goodspace.backend.admin.dto.item;

import goodspace.backend.client.domain.RegisterStatus;
import lombok.Builder;

@Builder
public record ItemUpdateRequestDto(
        Long clientId,
        Long itemId,
        String name,
        Integer price,
        String shortDescription,
        String landingPageDescription,
        RegisterStatus status
) {
}
