package goodspace.backend.email.dto;

public record VerifyRequestDto(
        String email,
        String code
) {
}
