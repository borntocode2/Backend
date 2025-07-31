package goodspace.backend.user.service;

import goodspace.backend.authorization.password.PasswordValidator;
import goodspace.backend.user.domain.GoodSpaceUser;
import goodspace.backend.user.domain.User;
import goodspace.backend.user.dto.PasswordUpdateRequestDto;
import goodspace.backend.user.dto.UserMyPageDto;
import goodspace.backend.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final Supplier<EntityNotFoundException> USER_NOT_FOUND = () -> new EntityNotFoundException("회원을 조회할 수 없습니다.");
    private static final Supplier<IllegalArgumentException> ILLEGAL_PASSWORD = () -> new IllegalArgumentException("부적절한 비밀번호입니다.");

    private final UserRepository userRepository;
    private final PasswordValidator passwordValidator;

    public String updateMyPage(Long id, UserMyPageDto userMyPageDto) {
        User user = userRepository.findById(id)
                .orElseThrow(USER_NOT_FOUND);

        user.setUserFromUserMyPageDto(userMyPageDto);

        return "유저 정보가 수정되었습니다.";
    }

    @Transactional
    public void updatePassword(Long id, PasswordUpdateRequestDto requestDto) {
        GoodSpaceUser user = userRepository.findGoodSpaceUserById(id)
                .orElseThrow(USER_NOT_FOUND);
        String password = requestDto.password();

        if (passwordValidator.isIllegalPassword(password)) {
            throw ILLEGAL_PASSWORD.get();
        }

        user.updatePassword(password);
    }
}
