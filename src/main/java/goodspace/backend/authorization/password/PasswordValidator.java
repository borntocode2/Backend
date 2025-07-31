package goodspace.backend.authorization.password;

public interface PasswordValidator {
    boolean isIllegalPassword(String password);
}
