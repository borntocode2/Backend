package goodspace.backend.admin.dto.client;

import goodspace.backend.client.domain.ClientType;
import goodspace.backend.client.domain.RegisterStatus;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record ClientUpdateRequestDto(
        Long id,
        String name,
        MultipartFile profileImage,
        MultipartFile backgroundImage,
        String introduction,
        ClientType clientType,
        RegisterStatus status
) {
}
