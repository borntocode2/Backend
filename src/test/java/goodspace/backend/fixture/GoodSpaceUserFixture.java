package goodspace.backend.fixture;

import goodspace.backend.domain.user.GoodSpaceUser;
import goodspace.backend.security.Role;

public enum GoodSpaceUserFixture {
    DEFAULT(
        "default@email.com",
            "helloPassword!",
            Role.USER
    );

    private final String email;
    private final String password;
    private final Role role;

    GoodSpaceUserFixture(String email, String password, Role role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public GoodSpaceUser getInstance() {
        GoodSpaceUser user = GoodSpaceUser.builder()
                .email(email)
                .password(password)
                .build();

        user.addRole(role);

        return user;
    }
}
