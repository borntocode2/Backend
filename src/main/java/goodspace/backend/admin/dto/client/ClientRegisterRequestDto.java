package goodspace.backend.admin.dto.client;

import goodspace.backend.client.domain.ClientType;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record ClientRegisterRequestDto(
        String name,
        MultipartFile profileImage,
        MultipartFile backgroundImage,
        String introduction,
        ClientType clientType
) {
}
