package goodspace.backend.email.dto;

import lombok.Builder;

@Builder
public record CodeSendRequestDto(
        String email
) {
}
