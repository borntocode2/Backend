package goodspace.backend.client.dto;

import goodspace.backend.global.domain.Item;
import lombok.Builder;

import java.util.List;

@Builder
public record ItemBriefInfoResponseDto(
        String name,
        String landingPageDescription,
        List<String> imageUrls
) {
    public static ItemBriefInfoResponseDto from(Item item) {
        return ItemBriefInfoResponseDto.builder()
                .name(item.getName())
                .landingPageDescription(item.getLandingPageDescription())
                .imageUrls(item.getImageUrls())
                .build();
    }
}
