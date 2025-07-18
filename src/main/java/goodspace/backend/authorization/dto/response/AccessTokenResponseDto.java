package goodspace.backend.authorization.dto.response;

import lombok.Builder;

@Builder
public record AccessTokenResponseDto(
        String accessToken
) {
}
