package goodspace.backend.user.dto;

import lombok.Builder;

@Builder
public record PasswordUpdateRequestDto(
        String prevPassword,
        String newPassword
) {
}
