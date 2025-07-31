package goodspace.backend.user.service;

import goodspace.backend.user.domain.User;
import goodspace.backend.user.dto.UserMyPageDto;
import goodspace.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public String updateMyPage(Long id, UserMyPageDto userMyPageDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found while updating MyPage Information."));

        user.setUserFromUserMyPageDto(userMyPageDto);

        return "유저 정보가 수정되었습니다.";
    }
}
