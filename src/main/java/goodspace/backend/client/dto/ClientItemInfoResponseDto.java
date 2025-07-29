package goodspace.backend.client.dto;

import goodspace.backend.client.domain.Client;
import goodspace.backend.global.domain.Item;
import lombok.Builder;

import java.util.List;

// TODO: 배송 관련 정보를 추가해야 함
@Builder
public record ClientItemInfoResponseDto(
        ClientDto client,
        ItemDto item
) {
    @Builder
    public record ClientDto(
            long id,
            String name,
            String profileImageUrl
    ) {
    }

    @Builder
    public record ItemDto(
            long id,
            String name,
            Integer price,
            String shortDescription,
            String landingPageDescription,
            List<String> imageUrls
    ) {
    }

    public static ClientItemInfoResponseDto of(Client client, Item item) {
        ClientDto clientDto = ClientDto.builder()
                .id(client.getId())
                .name(client.getName())
                .profileImageUrl(client.getProfileImageUrl())
                .build();

        ItemDto itemDto = ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .price(item.getPrice())
                .shortDescription(item.getShortDescription())
                .landingPageDescription(item.getLandingPageDescription())
                .imageUrls(item.getEveryImageUrl())
                .build();

        return ClientItemInfoResponseDto.builder()
                .client(clientDto)
                .item(itemDto)
                .build();
    }
}
