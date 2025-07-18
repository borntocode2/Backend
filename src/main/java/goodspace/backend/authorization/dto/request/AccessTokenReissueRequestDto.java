package goodspace.backend.authorization.dto.request;

public record AccessTokenReissueRequestDto(
    String refreshToken
) {
}
