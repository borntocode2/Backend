package goodspace.backend.admin.dto.client;

import goodspace.backend.domain.client.ClientType;
import lombok.Builder;

@Builder
public record ClientRegisterRequestDto(
        String name,
        String encodedProfileImage,
        String encodedBackgroundImage,
        String introduction,
        ClientType clientType
) {
}
