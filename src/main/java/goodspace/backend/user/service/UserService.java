package goodspace.backend.user.service;

import goodspace.backend.email.entity.EmailVerification;
import goodspace.backend.email.repository.EmailVerificationRepository;
import goodspace.backend.global.password.PasswordValidator;
import goodspace.backend.global.security.TokenProvider;
import goodspace.backend.global.security.TokenType;
import goodspace.backend.user.domain.Delivery;
import goodspace.backend.user.domain.GoodSpaceUser;
import goodspace.backend.user.domain.User;
import goodspace.backend.user.dto.*;
import goodspace.backend.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final Supplier<EntityNotFoundException> USER_NOT_FOUND = () -> new EntityNotFoundException("회원을 조회할 수 없습니다.");
    private static final Supplier<IllegalArgumentException> WRONG_PASSWORD = () -> new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
    private static final Supplier<IllegalArgumentException> ILLEGAL_PASSWORD = () -> new IllegalArgumentException("부적절한 비밀번호입니다.");
    private static final Supplier<EntityNotFoundException> VERIFICATION_NOT_FOUND = () -> new EntityNotFoundException("이메일 인증 정보를 찾을 수 없습니다.");
    private static final Supplier<IllegalStateException> NOT_VERIFIED = () -> new IllegalStateException("인증되지 않은 이메일입니다.");

    private final UserRepository userRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final PasswordValidator passwordValidator;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Transactional
    public UserMyPageResponseDto getUserInfo(long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found while getting information"));

        Delivery delivery = user.getDelivery();


        UserMyPageResponseDto.UserMyPageResponseDtoBuilder builder = UserMyPageResponseDto.builder()
                .dateOfBirth(user.getDateOfBirth())
                .email(user.getEmail())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber());

        if (delivery != null) {
            builder.recipient(delivery.getRecipient())
                    .address(delivery.getAddress())
                    .contactNumber2(delivery.getContactNumber2())
                    .contactNumber1(delivery.getContactNumber1())
                    .detailedAddress(delivery.getDetailedAddress())
                    .postalCode(delivery.getPostalCode());
        } else {
            builder.recipient(null)
                    .address(null)
                    .contactNumber2(null)
                    .contactNumber1(null)
                    .detailedAddress(null)
                    .postalCode(null);
        }

        return builder.build();
    }

    @Transactional
    public String updateMyPage(long userId, UserMyPageDto userMyPageDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found while updating MyPage Information."));

        user.setUserFromUserMyPageDto(userMyPageDto);

        return "유저 정보가 수정되었습니다.";
    }

    @Transactional
    public RefreshTokenResponseDto updatePassword(long userId, PasswordUpdateRequestDto requestDto) {
        GoodSpaceUser user = userRepository.findGoodSpaceUserById(userId)
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

    @Transactional
    public RefreshTokenResponseDto updateEmail(long userId, EmailUpdateRequestDto requestDto) {
        checkEmailVerification(requestDto.email());

        User user = userRepository.findById(userId)
                .orElseThrow(USER_NOT_FOUND);

        user.setEmail(requestDto.email());

        return RefreshTokenResponseDto.builder()
                .refreshToken(createNewRefreshToken(user))
                .build();
    }

    @Transactional(readOnly = true)
    public List<PurchaseHistoryResponseDto> getPurchaseHistory(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(USER_NOT_FOUND);

        return user.getOrders().stream()
                .map(PurchaseHistoryResponseDto::from)
                .toList();
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

    private void checkEmailVerification(String email) {
        EmailVerification emailVerification = emailVerificationRepository.findByEmail(email)
                .orElseThrow(VERIFICATION_NOT_FOUND);

        if (!emailVerification.isVerified()) {
            throw NOT_VERIFIED.get();
        }

        emailVerificationRepository.delete(emailVerification);
    }
}
