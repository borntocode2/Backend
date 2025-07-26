package goodspace.backend.admin.dto.item;

import goodspace.backend.client.domain.RegisterStatus;
import goodspace.backend.global.domain.Item;
import lombok.Builder;

import java.util.List;

@Builder
public record ItemInfoResponseDto(
        Long id,
        String name,
        Integer price,
        String shortDescription,
        String landingPageDescription,
        RegisterStatus status,
        List<String> imageUrls
) {
    public static ItemInfoResponseDto from(Item item) {
        return ItemInfoResponseDto.builder()
                .id(item.getId())
                .name(item.getName())
                .price(item.getPrice())
                .shortDescription(item.getShortDescription())
                .landingPageDescription(item.getLandingPageDescription())
                .status(item.getStatus())
                .imageUrls(item.getImageUrls())
                .build();
    }
}
