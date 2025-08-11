package goodspace.backend.global.password;

public interface PasswordValidator {
    boolean isIllegalPassword(String password);
}
