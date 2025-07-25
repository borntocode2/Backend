package goodspace.backend.admin.dto.item;

import lombok.Builder;

@Builder
public record ItemDeleteRequestDto(
        Long clientId,
        Long itemId
) {
}
