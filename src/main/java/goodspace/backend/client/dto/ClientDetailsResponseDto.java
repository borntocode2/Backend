package goodspace.backend.client.dto;

import goodspace.backend.domain.client.Client;
import lombok.Builder;

import java.util.List;

@Builder
public record ClientDetailsResponseDto(
        String name,
        String profileImageUrl,
        String backgroundImageUrl,
        String introduction,
        List<ItemBriefInfoResponseDto> items
) {
    public static ClientDetailsResponseDto from(Client client) {
        List<ItemBriefInfoResponseDto> items = client.getItems().stream()
                .map(ItemBriefInfoResponseDto::from)
                .toList();

        return ClientDetailsResponseDto.builder()
                .name(client.getName())
                .profileImageUrl(client.getProfileImageUrl())
                .backgroundImageUrl(client.getBackgroundImageUrl())
                .introduction(client.getIntroduction())
                .items(items)
                .build();
    }
}
