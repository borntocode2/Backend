package goodspace.backend.admin.dto.itemImage;

import lombok.Builder;

@Builder
public record ItemImageDeleteRequestDto(
        Long itemId,
        Long itemImageId
) {
}
