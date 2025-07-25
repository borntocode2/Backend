package goodspace.backend.authorization.dto.request;

import goodspace.backend.user.domain.GoodSpaceUser;

public record SignUpRequestDto(
        String email,
        String password
) {
    public GoodSpaceUser toEntity() {
        return GoodSpaceUser.builder()
                .email(email)
                .password(password)
                .build();
    }
}
