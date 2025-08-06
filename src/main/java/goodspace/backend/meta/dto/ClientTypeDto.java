package goodspace.backend.meta.dto;

import goodspace.backend.client.domain.ClientType;
import lombok.Builder;

@Builder
public record ClientTypeDto(
        String name,
        String korean
) {
    public static ClientTypeDto from(ClientType clientType) {
        return ClientTypeDto.builder()
                .name(clientType.name())
                .korean(clientType.getKorean())
                .build();
    }
}
