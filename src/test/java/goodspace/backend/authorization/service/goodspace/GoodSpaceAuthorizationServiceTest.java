package goodspace.backend.authorization.service.goodspace;

import goodspace.backend.authorization.dto.request.SignInRequestDto;
import goodspace.backend.authorization.dto.request.SignUpRequestDto;
import goodspace.backend.authorization.dto.response.TokenResponseDto;
import goodspace.backend.email.entity.EmailVerification;
import goodspace.backend.email.repository.EmailVerificationRepository;
import goodspace.backend.fixture.EmailVerificationFixture;
import goodspace.backend.fixture.GoodSpaceUserFixture;
import goodspace.backend.global.security.TokenProvider;
import goodspace.backend.global.security.TokenType;
import goodspace.backend.user.domain.GoodSpaceUser;
import goodspace.backend.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GoodSpaceAuthorizationServiceTest {
    private static final Supplier<EntityNotFoundException> ENTITY_NOT_FOUND = () -> new EntityNotFoundException("엔티티를 조회할 수 없습니다.");

    private static final String DEFAULT_PASSWORD = "HelloWorldJava1!";

    private final GoodSpaceAuthorizationService authorizationService;
    private final EmailVerificationRepository emailVerificationRepository;
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    private EmailVerification verifiedEmail;
    private EmailVerification notVerifiedEmail;
    private GoodSpaceUser existUser;

    @BeforeEach
    void resetEntities() {
        verifiedEmail = createEntityFromFixture(EmailVerificationFixture.VERIFIED);
        notVerifiedEmail = createEntityFromFixture(EmailVerificationFixture.NOT_VERIFIED);
        existUser = createEntityFromFixture(GoodSpaceUserFixture.DEFAULT);
        existUser.updatePassword(passwordEncoder.encode(DEFAULT_PASSWORD));
    }

    @Nested
    class signUp {
        @Test
        @DisplayName("인증된 이메일로 새로운 회원을 생성한다")
        void createUserWithVerifiedEmail() {
            // given
            String email = verifiedEmail.getEmail();

            // when
            authorizationService.signUp(new SignUpRequestDto(email, DEFAULT_PASSWORD));

            // then
            GoodSpaceUser user = userRepository.findGoodSpaceUserByEmail(email)
                    .orElseThrow(ENTITY_NOT_FOUND);

            assertThat(isSamePassword(DEFAULT_PASSWORD, user.getPassword())).isTrue();
        }

        @Test
        @DisplayName("인증되지 않은 이메일을 사용하면 예외가 발생한다")
        void ifNotVerifiedEmailThenThrowException() {
            // given
            String email = notVerifiedEmail.getEmail();

            // then
            assertThatThrownBy(() -> authorizationService.signUp(new SignUpRequestDto(email, DEFAULT_PASSWORD)))
                    .isInstanceOf(IllegalStateException.class);
        }

        @Test
        @DisplayName("유저 생성 후 인증 정보는 제거한다")
        void deleteVerificationInformationAfterCreateUser() {
            // given
            String email = verifiedEmail.getEmail();

            // when
            authorizationService.signUp(new SignUpRequestDto(email, DEFAULT_PASSWORD));

            // then
            boolean isEmpty = emailVerificationRepository.findByEmail(email)
                    .isEmpty();
            assertThat(isEmpty).isTrue();
        }

        @Test
        @DisplayName("회원 생성에 성공하면 JWT 토큰을 발급한다")
        void issueJwtIfUserCreateSucceeded() {
            // given
            String email = verifiedEmail.getEmail();

            // when
            TokenResponseDto jwt = authorizationService.signUp(new SignUpRequestDto(email, DEFAULT_PASSWORD));

            // then
            boolean isLegalAccessToken = tokenProvider.validateToken(jwt.accessToken(), TokenType.ACCESS);
            boolean isLegalRefreshToken = tokenProvider.validateToken(jwt.refreshToken(), TokenType.REFRESH);

            assertThat(isLegalAccessToken).isTrue();
            assertThat(isLegalRefreshToken).isTrue();
        }
    }

    @Nested
    class signIn {
        @Test
        @DisplayName("이메일과 비밀번호가 올바르면 JWT 토큰을 발급한다")
        void issueJwtIfEmailAndPasswordAreCorrect() {
            // given
            String email = existUser.getEmail();

            // when
            TokenResponseDto jwt = authorizationService.signIn(new SignInRequestDto(email, DEFAULT_PASSWORD));

            // then
            boolean isLegalAccessToken = tokenProvider.validateToken(jwt.accessToken(), TokenType.ACCESS);
            boolean isLegalRefreshToken = tokenProvider.validateToken(jwt.refreshToken(), TokenType.REFRESH);

            assertThat(isLegalAccessToken).isTrue();
            assertThat(isLegalRefreshToken).isTrue();
        }

        @Test
        @DisplayName("리프레쉬 토큰을 새로 갱신한다")
        void updateRefreshToken() throws InterruptedException {
            // given
            String existRefreshToken = existUser.getRefreshToken();
            String email = existUser.getEmail();

            sleepForGetDifferentToken();

            // when
            authorizationService.signIn(new SignInRequestDto(email, DEFAULT_PASSWORD));

            // then
            String newRefreshToken = existUser.getRefreshToken();
            assertThat(newRefreshToken).isNotEqualTo(existRefreshToken);
        }
    }

    private EmailVerification createEntityFromFixture(EmailVerificationFixture fixture) {
        EmailVerification instance = fixture.getInstance();
        emailVerificationRepository.save(instance);

        return emailVerificationRepository.findByEmail(instance.getEmail())
                .orElseThrow();
    }

    private GoodSpaceUser createEntityFromFixture(GoodSpaceUserFixture fixture) {
        GoodSpaceUser user = fixture.getInstance();
        userRepository.save(user);

        String refreshToken = tokenProvider.createToken(user.getId(), TokenType.REFRESH, user.getRoles());
        user.updateRefreshToken(refreshToken);

        return userRepository.findGoodSpaceUserById(user.getId())
                .orElseThrow();
    }

    private void sleepForGetDifferentToken() throws InterruptedException {
        Thread.sleep(1000);
    }

    private boolean isSamePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
