package goodspace.backend.authorization.dto.request;

public record SignInRequestDto(
        String email,
        String password
) {
}
