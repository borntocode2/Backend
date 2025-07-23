package goodspace.backend.client.dto;

import goodspace.backend.domain.client.Client;
import goodspace.backend.domain.client.ClientType;
import lombok.Builder;

@Builder
public record ClientBriefInfoResponseDto(
        Long id,
        String name,
        String profileImageUrl,
        ClientType clientType
) {
    public static ClientBriefInfoResponseDto from(Client client) {
        return ClientBriefInfoResponseDto.builder()
                .id(client.getId())
                .name(client.getName())
                .profileImageUrl(client.getProfileImageUrl())
                .clientType(client.getClientType())
                .build();
    }
}
