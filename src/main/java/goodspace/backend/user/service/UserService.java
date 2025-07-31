package goodspace.backend.user.service;

import goodspace.backend.global.password.PasswordValidator;
import goodspace.backend.global.security.TokenProvider;
import goodspace.backend.global.security.TokenType;
import goodspace.backend.user.domain.GoodSpaceUser;
import goodspace.backend.user.domain.User;
import goodspace.backend.user.dto.PasswordUpdateRequestDto;
import goodspace.backend.user.dto.RefreshTokenResponseDto;
import goodspace.backend.user.dto.UserMyPageDto;
import goodspace.backend.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final Supplier<EntityNotFoundException> USER_NOT_FOUND = () -> new EntityNotFoundException("회원을 조회할 수 없습니다.");
    private static final Supplier<IllegalArgumentException> WRONG_PASSWORD = () -> new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
    private static final Supplier<IllegalArgumentException> ILLEGAL_PASSWORD = () -> new IllegalArgumentException("부적절한 비밀번호입니다.");

    private final UserRepository userRepository;
    private final PasswordValidator passwordValidator;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public String updateMyPage(Long id, UserMyPageDto userMyPageDto) {
        User user = userRepository.findById(id)
                .orElseThrow(USER_NOT_FOUND);

        user.setUserFromUserMyPageDto(userMyPageDto);

        return "유저 정보가 수정되었습니다.";
    }

    @Transactional
    public RefreshTokenResponseDto updatePassword(Long id, PasswordUpdateRequestDto requestDto) {
        GoodSpaceUser user = userRepository.findGoodSpaceUserById(id)
                .orElseThrow(USER_NOT_FOUND);
        String rawPrevPassword = requestDto.prevPassword();
        String rawNewPassword = requestDto.newPassword();

        if (isDifferentPassword(rawPrevPassword, user.getPassword())) {
            throw WRONG_PASSWORD.get();
        }

        validatePassword(rawNewPassword);

        String encodedPassword = passwordEncoder.encode(rawNewPassword);
        user.updatePassword(encodedPassword);

        return RefreshTokenResponseDto.builder()
                .refreshToken(createNewRefreshToken(user))
                .build();
    }

    private void validatePassword(String rawPassword) {
        if (passwordValidator.isIllegalPassword(rawPassword)) {
            throw ILLEGAL_PASSWORD.get();
        }
    }

    private boolean isDifferentPassword(String rawPassword, String encodedPassword) {
        return !passwordEncoder.matches(rawPassword, encodedPassword);
    }

    private String createNewRefreshToken(User user) {
        String refreshToken = tokenProvider.createToken(user.getId(), TokenType.REFRESH, user.getRoles());
        user.updateRefreshToken(refreshToken);

        return refreshToken;
    }
}
