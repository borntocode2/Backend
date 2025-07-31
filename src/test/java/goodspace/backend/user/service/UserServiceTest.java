package goodspace.backend.user.service;

import goodspace.backend.email.entity.EmailVerification;
import goodspace.backend.email.repository.EmailVerificationRepository;
import goodspace.backend.fixture.EmailVerificationFixture;
import goodspace.backend.fixture.GoodSpaceUserFixture;
import goodspace.backend.user.domain.GoodSpaceUser;
import goodspace.backend.user.dto.EmailUpdateRequestDto;
import goodspace.backend.user.dto.PasswordUpdateRequestDto;
import goodspace.backend.user.dto.RefreshTokenResponseDto;
import goodspace.backend.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class UserServiceTest {
    static final String DEFAULT_PASSWORD = "HelloPassword1!";
    static final String DEFAULT_REFRESH_TOKEN = "defaultRefreshToken";

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    EmailVerificationRepository emailVerificationRepository;

    GoodSpaceUser user;
    EmailVerification verifiedEmail;
    EmailVerification notVerifiedEmail;

    @BeforeEach
    void resetEntities() {
        user = userRepository.save(GoodSpaceUserFixture.DEFAULT.getInstance());
        user.updatePassword(passwordEncoder.encode(DEFAULT_PASSWORD));
        user.updateRefreshToken(DEFAULT_REFRESH_TOKEN);

        verifiedEmail = emailVerificationRepository.save(EmailVerificationFixture.VERIFIED.getInstance());
        notVerifiedEmail = emailVerificationRepository.save(EmailVerificationFixture.NOT_VERIFIED.getInstance());
    }

    @Nested
    class updatePassword {
        @Test
        @DisplayName("비밀번호를 변경한다")
        void changePassword() {
            // given
            String newPassword = "HelloNewPassword1!";
            PasswordUpdateRequestDto requestDto = PasswordUpdateRequestDto.builder()
                    .prevPassword(DEFAULT_PASSWORD)
                    .newPassword(newPassword)
                    .build();

            // when
            userService.updatePassword(user.getId(), requestDto);

            // then
            assertThat(isSamePassword(newPassword, user.getPassword())).isTrue();
        }
    }

    @Nested
    class updateEmail {
        @Test
        @DisplayName("이메일을 변경한다")
        void changeEmail() {
            // given
            EmailUpdateRequestDto requestDto = EmailUpdateRequestDto.builder()
                    .email(verifiedEmail.getEmail())
                    .build();

            // when
            userService.updateEmail(user.getId(), requestDto);

            // then
            assertThat(user.getEmail()).isEqualTo(verifiedEmail.getEmail());
        }

        @Test
        @DisplayName("리프레쉬 토큰을 새로 발급한다")
        void reissueRefreshToken() {
            // given
            EmailUpdateRequestDto requestDto = EmailUpdateRequestDto.builder()
                    .email(verifiedEmail.getEmail())
                    .build();

            // when
            RefreshTokenResponseDto tokenDto = userService.updateEmail(user.getId(), requestDto);

            // then
            assertThat(tokenDto.refreshToken()).isNotEqualTo(DEFAULT_REFRESH_TOKEN);
        }

        @Test
        @DisplayName("인증되지 않은 이메일이라면 예외가 발생한다")
        void ifNotVerifiedEmailThenThrowException() {
            EmailUpdateRequestDto requestDto = EmailUpdateRequestDto.builder()
                    .email(notVerifiedEmail.getEmail())
                    .build();

            assertThatThrownBy(() -> userService.updateEmail(user.getId(), requestDto))
                    .isInstanceOf(IllegalStateException.class);
        }
    }

    private boolean isSamePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
