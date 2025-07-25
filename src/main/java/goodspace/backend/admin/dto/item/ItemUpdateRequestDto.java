package goodspace.backend.admin.dto.item;

import lombok.Builder;

@Builder
public record ItemUpdateRequestDto(
        Long clientId,
        Long itemId,
        String name,
        Integer price,
        String shortDescription,
        String landingPageDescription
) {
}
