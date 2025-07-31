package goodspace.backend.authorization.service.goodspace;

import goodspace.backend.authorization.dto.request.SignInRequestDto;
import goodspace.backend.authorization.dto.request.SignUpRequestDto;
import goodspace.backend.authorization.dto.response.TokenResponseDto;
import goodspace.backend.authorization.password.PasswordValidator;
import goodspace.backend.user.domain.GoodSpaceUser;
import goodspace.backend.email.entity.EmailVerification;
import goodspace.backend.email.repository.EmailVerificationRepository;
import goodspace.backend.user.repository.UserRepository;
import goodspace.backend.global.security.TokenProvider;
import goodspace.backend.global.security.Role;
import goodspace.backend.global.security.TokenType;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class GoodSpaceAuthorizationServiceImpl implements GoodSpaceAuthorizationService {
    private static final Supplier<EntityNotFoundException> VERIFICATION_NOT_FOUND = () -> new EntityNotFoundException("이메일 인증 정보가 없습니다.");
    private static final Supplier<IllegalStateException> NOT_VERIFIED = () -> new IllegalStateException("이메일이 인증되지 않았습니다.");
    private static final Supplier<EntityNotFoundException> USER_NOT_FOUND = () -> new EntityNotFoundException("회원을 찾을 수 없습니다.");
    private static final Supplier<IllegalArgumentException> ILLEGAL_PASSWORD = () -> new IllegalArgumentException("부적절한 비밀번호입니다.");

    private final UserRepository userRepository;
    private final EmailVerificationRepository emailVerificationRepository;

    private final TokenProvider tokenProvider;
    private final PasswordValidator passwordValidator;

    @Override
    @Transactional
    public TokenResponseDto signUp(SignUpRequestDto requestDto) {
        checkEmailVerification(requestDto.email());
        checkPassword(requestDto.password());

        GoodSpaceUser user = requestDto.toEntity();
        user.addRole(Role.USER);
        userRepository.save(user);

        String accessToken = tokenProvider.createToken(user.getId(), TokenType.ACCESS, user.getRoles());
        String refreshToken = tokenProvider.createToken(user.getId(), TokenType.REFRESH, user.getRoles());
        user.updateRefreshToken(refreshToken);

        return TokenResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public TokenResponseDto signIn(SignInRequestDto requestDto) {
        GoodSpaceUser user = userRepository.findByEmailAndPassword(requestDto.email(), requestDto.password())
                .orElseThrow(USER_NOT_FOUND);

        String accessToken = tokenProvider.createToken(user.getId(), TokenType.ACCESS, user.getRoles());
        String refreshToken = tokenProvider.createToken(user.getId(), TokenType.REFRESH, user.getRoles());
        user.updateRefreshToken(refreshToken);

        return TokenResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void checkEmailVerification(String email) {
        EmailVerification emailVerification = emailVerificationRepository.findByEmail(email)
                .orElseThrow(VERIFICATION_NOT_FOUND);

        if (!emailVerification.isVerified()) {
            throw NOT_VERIFIED.get();
        }

        emailVerificationRepository.delete(emailVerification);
    }

    private void checkPassword(String password) {
        if (passwordValidator.isIllegalPassword(password)) {
            throw ILLEGAL_PASSWORD.get();
        }
    }
}
