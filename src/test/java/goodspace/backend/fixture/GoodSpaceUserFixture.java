package goodspace.backend.fixture;

import goodspace.backend.domain.user.GoodSpaceUser;

public enum GoodSpaceUserFixture {
    DEFAULT(
        "default@email.com",
            "helloPassword!"
    );

    private final String email;
    private final String password;

    GoodSpaceUserFixture(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public GoodSpaceUser getInstance() {
        return GoodSpaceUser.builder()
                .email(email)
                .password(password)
                .build();
    }
}
