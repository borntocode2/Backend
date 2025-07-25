package goodspace.backend.admin.dto.item;

import lombok.Builder;

@Builder
public record ItemRegisterRequestDto(
        Long clientId,
        String name,
        Integer price,
        String shortDescription,
        String landingPageDescription
) {
}
