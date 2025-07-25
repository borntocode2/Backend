package goodspace.backend.global.security;

public enum Role {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private final String string;

    Role(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return string;
    }
}
