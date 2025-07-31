package goodspace.backend.admin.dto.itemImage;

import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record ItemImageRegisterRequestDto(
        Long clientId,
        Long itemId,
        MultipartFile image
) {
}
