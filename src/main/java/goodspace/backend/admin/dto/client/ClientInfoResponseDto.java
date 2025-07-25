package goodspace.backend.admin.dto.client;

import goodspace.backend.domain.client.Client;
import goodspace.backend.domain.client.ClientType;
import lombok.Builder;

@Builder
public record ClientInfoResponseDto(
        Long id,
        String name,
        String profileImageUrl,
        String backgroundImageUrl,
        String introduction,
        ClientType clientType
) {
    public static ClientInfoResponseDto from(Client client) {
        return ClientInfoResponseDto.builder()
                .id(client.getId())
                .name(client.getName())
                .profileImageUrl(client.getProfileImageUrl())
                .backgroundImageUrl(client.getBackgroundImageUrl())
                .introduction(client.getIntroduction())
                .clientType(client.getClientType())
                .build();
    }
}
