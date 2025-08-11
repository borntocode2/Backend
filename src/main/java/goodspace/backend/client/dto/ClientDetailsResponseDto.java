package goodspace.backend.client.dto;

import goodspace.backend.client.domain.Client;
import goodspace.backend.global.domain.Item;
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
    public static ClientDetailsResponseDto of(Client client, boolean completeStatusOnly) {
        List<ItemBriefInfoResponseDto> items = getQualifiedItems(client, completeStatusOnly).stream()
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

    private static List<Item> getQualifiedItems(Client client, boolean completeStatusOnly) {
        if (completeStatusOnly) {
            return client.getPublicItems();
        }

        return client.getItems();
    }
}
