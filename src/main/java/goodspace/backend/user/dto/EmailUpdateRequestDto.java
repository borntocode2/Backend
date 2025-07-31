package goodspace.backend.user.dto;

import lombok.Builder;

@Builder
public record EmailUpdateRequestDto(
        String email
) {
}
