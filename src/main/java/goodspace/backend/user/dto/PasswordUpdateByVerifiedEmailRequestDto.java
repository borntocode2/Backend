package goodspace.backend.user.dto;

import lombok.Builder;

@Builder
public record PasswordUpdateByVerifiedEmailRequestDto(
        String email,
        String password
) {
}
