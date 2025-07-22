package goodspace.backend.email.controller;

import goodspace.backend.email.dto.CodeSendRequestDto;
import goodspace.backend.email.dto.VerifyRequestDto;
import goodspace.backend.email.service.EmailVerificationService;
import goodspace.backend.email.service.EmailVerificationServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
@Tag(
        name = "이메일 API",
        description = "이메일 발송 및 인증 관련 기능"
)
public class EmailController {
    private final EmailVerificationService emailVerificationService;

    @PostMapping("/send-code")
    public ResponseEntity<Void> sendCode(@RequestBody CodeSendRequestDto requestDto) {
        try {
            emailVerificationService.sendVerificationCode(requestDto);
            return ResponseEntity.ok().build();
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/verify-code")
    public ResponseEntity<String> verifyCode(@RequestBody VerifyRequestDto requestDto) {
        emailVerificationService.verifyEmail(requestDto);

        return ResponseEntity.ok().build();
    }
}
