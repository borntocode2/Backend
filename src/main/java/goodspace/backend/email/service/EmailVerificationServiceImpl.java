package goodspace.backend.email.service;

import goodspace.backend.email.domain.CodeGenerator;
import goodspace.backend.email.domain.MessageBuilder;
import goodspace.backend.email.dto.CodeSendRequestDto;
import goodspace.backend.email.dto.VerifyRequestDto;
import goodspace.backend.email.entity.EmailVerification;
import goodspace.backend.email.repository.EmailVerificationRepository;
import goodspace.backend.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class EmailVerificationServiceImpl implements EmailVerificationService {
    private static final int CODE_LENGTH = 6;
    private static final int EXPIRE_MINUTES = 5;

    private static final Supplier<IllegalArgumentException> DUPLICATED_EMAIL = () -> new IllegalArgumentException("이미 존재하는 이메일입니다.");
    private static final Supplier<IllegalArgumentException> NOT_DUPLICATED_EMAIL = () -> new IllegalArgumentException("존재하지 않는 이메일입니다.");
    private static final Supplier<EntityNotFoundException> EMAIL_NOT_FOUND = () -> new EntityNotFoundException("이메일을 찾을 수 없습니다.");
    private static final Supplier<IllegalArgumentException> ILLEGAL_CODE = () -> new IllegalArgumentException("코드가 올바르지 않습니다.");
    private static final Supplier<IllegalStateException> EXPIRED = () -> new IllegalStateException("이메일 인증이 만료되었습니다.");

    private final EmailVerificationRepository emailVerificationRepository;
    private final UserRepository userRepository;

    private final JavaMailSender mailSender;
    private final MessageBuilder messageBuilder;
    private final CodeGenerator codeGenerator;

    @Override
    @Transactional
    public void sendVerificationCode(CodeSendRequestDto requestDto) throws MessagingException {
        String email = requestDto.email();

        validateDuplicate(email, requestDto.shouldAlreadyExist());

        String code = codeGenerator.generate(CODE_LENGTH);
        LocalDateTime now = LocalDateTime.now();

        EmailVerification emailVerification = EmailVerification.builder()
                .email(email)
                .code(code)
                .expiresAt(now.plusMinutes(EXPIRE_MINUTES))
                .createdAt(now)
                .isVerified(false)
                .build();

        updateOrCreate(emailVerification);
        sendEmail(email, code);
    }

    @Override
    @Transactional
    public void verifyEmail(VerifyRequestDto requestDto) {
        String email = requestDto.email();
        String code = requestDto.code();

        EmailVerification emailVerification = emailVerificationRepository.findByEmail(email)
                .orElseThrow(EMAIL_NOT_FOUND);

        if (emailVerification.isExpired(LocalDateTime.now())) {
            throw EXPIRED.get();
        }

        if (!emailVerification.hasSameCode(code)) {
            throw ILLEGAL_CODE.get();
        }

        emailVerification.verify();
    }

    private void validateDuplicate(String email, boolean shouldDuplicate) {
        if (shouldDuplicate && !isDuplicatedEmail(email)) {
            throw NOT_DUPLICATED_EMAIL.get();
        }

        if (!shouldDuplicate && isDuplicatedEmail(email)) {
            throw DUPLICATED_EMAIL.get();
        }
    }

    private boolean isDuplicatedEmail(String email) {
        return userRepository.findByEmail(email)
                .isPresent();
    }

    private void sendEmail(String to, String code) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        messageBuilder.fillContent(message, to, code, EXPIRE_MINUTES);

        mailSender.send(message);
    }

    private void updateOrCreate(EmailVerification emailVerification) {
        String email = emailVerification.getEmail();

        if (emailVerificationRepository.findByEmail(email).isEmpty()) {
            emailVerificationRepository.save(emailVerification);
            return;
        }

        EmailVerification existEmailVerification = emailVerificationRepository.findByEmail(email).get();

        existEmailVerification.setCode(emailVerification.getCode());
        existEmailVerification.setExpiresAt(emailVerification.getExpiresAt());
        existEmailVerification.setVerified(emailVerification.isVerified());
    }
}
