package goodspace.backend.fixture;

import goodspace.backend.email.entity.EmailVerification;

import java.time.LocalDateTime;

public enum EmailVerificationFixture {
    NOT_VERIFIED(
            "not@email.com",
            "123123",
            LocalDateTime.now().plusMinutes(5),
            false
    ),
    VERIFIED(
            "verified@email.com",
            "345345",
            LocalDateTime.now().plusMinutes(5),
            true
    ),
    NOT_EXPIRED(
        "yet@email.com",
        "789789",
        LocalDateTime.now().plusHours(1),
        false
    ),
    EXPIRED(
            "expired@email.com",
            "789789",
            LocalDateTime.now().minusMinutes(1),
            false
    );

    private final String email;
    private final String code;
    private final LocalDateTime expiresAt;
    private final boolean isVerified;

    EmailVerificationFixture(
            String email,
            String code,
            LocalDateTime expiresAt,
            boolean isVerified
    ) {
        this.email = email;
        this.code = code;
        this.expiresAt = expiresAt;
        this.isVerified = isVerified;
    }

    public EmailVerification getInstance() {
        return EmailVerification.builder()
                .email(email)
                .code(code)
                .expiresAt(expiresAt)
                .isVerified(isVerified)
                .build();
    }
}
