package goodspace.backend.admin.service.user;

import goodspace.backend.admin.dto.user.UserInfoDto;
import goodspace.backend.user.domain.User;
import goodspace.backend.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class UserManageServiceImpl implements UserManageService {
    private static final Supplier<EntityNotFoundException> USER_NOT_FOUND = () -> new EntityNotFoundException("회원을 찾을 수 없습니다.");

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UserInfoDto> getUsers() {
        return userRepository.findAll().stream()
                .map(UserInfoDto::from)
                .toList();
    }

    @Override
    @Transactional
    public void removeUser(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(USER_NOT_FOUND);

        userRepository.delete(user);
    }
}
