package goodspace.backend.admin.dto.client;

import goodspace.backend.client.domain.ClientType;
import lombok.Builder;

@Builder
public record ClientUpdateRequestDto(
        Long id,
        String name,
        String encodedProfileImage,
        boolean profileUpdated,
        String encodedBackgroundImage,
        boolean backgroundUpdated,
        String introduction,
        ClientType clientType
) {
}
