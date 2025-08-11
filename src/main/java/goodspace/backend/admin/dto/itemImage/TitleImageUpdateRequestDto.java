package goodspace.backend.admin.dto.itemImage;

import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record TitleImageUpdateRequestDto(
        Long itemId,
        MultipartFile image
) {
}
