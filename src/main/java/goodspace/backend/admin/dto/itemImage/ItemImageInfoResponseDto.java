package goodspace.backend.admin.dto.itemImage;

import goodspace.backend.global.domain.ItemImage;
import lombok.Builder;

@Builder
public record ItemImageInfoResponseDto(
        Long id,
        String imageUrl
) {
    public static ItemImageInfoResponseDto from(ItemImage itemImage) {
        return ItemImageInfoResponseDto.builder()
                .id(itemImage.getId())
                .imageUrl(itemImage.getImageUrl())
                .build();
    }
}
