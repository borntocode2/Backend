package goodspace.backend.email.service;

import goodspace.backend.email.dto.CodeSendRequestDto;
import goodspace.backend.email.dto.VerifyRequestDto;
import goodspace.backend.email.entity.EmailVerification;
import goodspace.backend.email.repository.EmailVerificationRepository;
import goodspace.backend.fixture.EmailVerificationFixture;
import goodspace.backend.fixture.GoodSpaceUserFixture;
import goodspace.backend.user.domain.GoodSpaceUser;
import goodspace.backend.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class EmailVerificationServiceTest {
    static final String NOISE = "2193812555555123";

    @Autowired
    EmailVerificationService emailVerificationService;
    @Autowired
    EmailVerificationRepository emailVerificationRepository;
    @Autowired
    UserRepository userRepository;

    String notExistEmail = "NOT_EXIST@email.com";
    String existEmail;
    EmailVerification existEmailVerification;

    @BeforeEach
    void resetEntities() {
        GoodSpaceUser user = userRepository.save(GoodSpaceUserFixture.DEFAULT.getInstance());
        existEmail = user.getEmail();

        existEmailVerification = emailVerificationRepository.save(EmailVerificationFixture.NOT_VERIFIED.getInstance());
        existEmailVerification.setCode(NOISE);
    }

    @Nested
    class sendVerificationCode {
        @Test
        @DisplayName("이메일 인증 정보 객체를 생성한다")
        void createEmailVerificationEntity() throws MessagingException {
            // given
            CodeSendRequestDto requestDto = CodeSendRequestDto.builder()
                    .email(notExistEmail)
                    .shouldAlreadyExist(false)
                    .build();

            // when
            emailVerificationService.sendVerificationCode(requestDto);

            // then
            boolean isPresent = emailVerificationRepository.findByEmail(requestDto.email())
                    .isPresent();
            assertThat(isPresent).isTrue();
        }

        @Test
        @DisplayName("ShouldAlreadyExist가 false이면서 이미 회원가입된 이메일이라면 예외를 던진다")
        void ifShouldNotAlreadyExistEmailUsedEmailThenThrowException() {
            CodeSendRequestDto requestDto = CodeSendRequestDto.builder()
                    .email(existEmail)
                    .shouldAlreadyExist(false)
                    .build();

            assertThatThrownBy(() -> emailVerificationService.sendVerificationCode(requestDto))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("ShouldAlreadyExist가 true이면서 회원가입되지 않은 이메일이라면 예외를 던진다")
        void ifShouldAlreadyExistEmailNotUsedEmailThenThrowException() {
            CodeSendRequestDto requestDto = CodeSendRequestDto.builder()
                    .email(notExistEmail)
                    .shouldAlreadyExist(true)
                    .build();

            assertThatThrownBy(() -> emailVerificationService.sendVerificationCode(requestDto))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("기존에 같은 이메일의 EmailVerification이 존재한다면 덮어써 갱신한다")
        void idExistEmailVerificationThenUpdate() throws MessagingException {
            // given
            String existEmail = existEmailVerification.getEmail();
            LocalDateTime existExpiredTime = existEmailVerification.getExpiresAt();
            String existCode = existEmailVerification.getCode();

            CodeSendRequestDto requestDto = CodeSendRequestDto.builder()
                    .email(existEmail)
                    .build();

            // when
            emailVerificationService.sendVerificationCode(requestDto);

            // then
            LocalDateTime updatedExpiredTime = existEmailVerification.getExpiresAt();
            String updatedCode = existEmailVerification.getCode();

            assertThat(updatedExpiredTime).isNotEqualTo(existExpiredTime);
            assertThat(updatedCode).isNotEqualTo(existCode);
        }
    }

    @Nested
    class verifyEmail {
        @Test
        @DisplayName("코드가 일치하면 이메일을 인증 처리 한다")
        void ifCodeSameThenVerifyEmail() {
            // given
            String email = existEmailVerification.getEmail();
            String correctCode = existEmailVerification.getCode();

            // when
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
            String email = existEmailVerification.getEmail();
            String wrongCode = existEmailVerification.getCode() + NOISE;

            // then
            assertThatThrownBy(() -> emailVerificationService.verifyEmail(new VerifyRequestDto(email, wrongCode)))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
