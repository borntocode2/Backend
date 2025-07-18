package goodspace.backend.email.service;

import goodspace.backend.email.dto.CodeSendRequestDto;
import goodspace.backend.email.dto.VerifyRequestDto;
import goodspace.backend.email.entity.EmailVerification;
import goodspace.backend.email.repository.EmailVerificationRepository;
import goodspace.backend.fixture.EmailFixture;
import goodspace.backend.fixture.EmailVerificationFixture;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class EmailVerificationServiceTest {
    private static final String NOISE = "2193812";

    private final EmailVerificationService emailVerificationService;
    private final EmailVerificationRepository emailVerificationRepository;

    @Nested
    class sendVerificationCode {
        @Test
        @DisplayName("이메일 인증 정보 객체를 생성한다")
        void createEmailVerificationEntity() throws MessagingException {
            // given
            CodeSendRequestDto requestDto = createDtoFromFixture(EmailFixture.USER_A);

            // when
            emailVerificationService.sendVerificationCode(requestDto);

            // then
            boolean isPresent = emailVerificationRepository.findByEmail(requestDto.email())
                    .isPresent();
            assertThat(isPresent).isTrue();
        }
    }

    @Nested
    class verifyEmail {
        @Test
        @DisplayName("코드가 일치하면 이메일을 인증 처리 한다")
        void ifCodeSameThenVerifyEmail() {
            // given
            EmailVerification emailVerification = EmailVerificationFixture.NOT_VERIFIED.getInstance();
            emailVerificationRepository.save(emailVerification);

            // when
            String email = emailVerification.getEmail();
            String correctCode = emailVerification.getCode();

            emailVerificationService.verifyEmail(new VerifyRequestDto(email, correctCode));

            // then
            EmailVerification verifedEmailVerification = emailVerificationRepository.findByEmail(email)
                    .orElseThrow();
            assertThat(verifedEmailVerification.isVerified()).isTrue();
        }

        @Test
        @DisplayName("코드가 일치하지 않으면 예외를 발생시킨다")
        void ifCodeDifferentThenThrowException() {
            // given
            EmailVerification emailVerification = EmailVerificationFixture.NOT_VERIFIED.getInstance();
            emailVerificationRepository.save(emailVerification);

            // when
            String email = emailVerification.getEmail();
            String wrongCode = emailVerification.getCode() + NOISE;

            // then
            assertThatThrownBy(() -> emailVerificationService.verifyEmail(new VerifyRequestDto(email, wrongCode)))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    private CodeSendRequestDto createDtoFromFixture(EmailFixture fixture) {
        return new CodeSendRequestDto(fixture.getEmail());
    }
}
