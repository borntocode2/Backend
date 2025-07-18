package goodspace.backend.email.service;

import goodspace.backend.email.dto.CodeSendRequestDto;
import goodspace.backend.email.dto.VerifyRequestDto;
import jakarta.mail.MessagingException;

public interface EmailVerificationService {
    void sendVerificationCode(CodeSendRequestDto requestDto) throws MessagingException;

    void verifyEmail(VerifyRequestDto requestDto);
}
