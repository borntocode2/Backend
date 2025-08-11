package goodspace.backend.fixture;

import goodspace.backend.user.domain.GoodSpaceUser;
import goodspace.backend.global.security.Role;

public enum GoodSpaceUserFixture {
    DEFAULT(
        "default@email.com",
            "HelloWorldJava1!",
            Role.USER
    ),
    A(
            "A@email.com",
            "HelloWorldJava1!a",
            Role.USER
    ),
    B(
            "B@email.com",
            "HelloWorldJava1!b",
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
