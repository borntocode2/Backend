package goodspace.backend.admin.dto.itemImage;

import lombok.Builder;

@Builder
public record ItemImageRegisterRequestDto(
        Long clientId,
        Long itemId,
        String encodedImage
) {
}
