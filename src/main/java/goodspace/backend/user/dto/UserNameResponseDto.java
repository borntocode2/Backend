package goodspace.backend.user.dto;

import goodspace.backend.user.domain.User;
import lombok.Builder;

@Builder
public record UserNameResponseDto(
        String name
) {
    public static UserNameResponseDto from(User user) {
        return UserNameResponseDto.builder()
                .name(user.getName())
                .build();
    }
}
