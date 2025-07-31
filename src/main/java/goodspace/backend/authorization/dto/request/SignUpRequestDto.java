package goodspace.backend.authorization.dto.request;

import goodspace.backend.user.domain.GoodSpaceUser;

public record SignUpRequestDto(
        String email,
        String password
) {
    public GoodSpaceUser toEntityWith(String encodedPassword) {
        return GoodSpaceUser.builder()
                .email(email)
                .password(encodedPassword)
                .build();
    }
}
