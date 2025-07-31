package goodspace.backend.user.service;

import goodspace.backend.fixture.GoodSpaceUserFixture;
import goodspace.backend.user.domain.GoodSpaceUser;
import goodspace.backend.user.dto.PasswordUpdateRequestDto;
import goodspace.backend.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UserServiceTest {
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    GoodSpaceUser user;

    @BeforeEach
    void resetEntities() {
        user = userRepository.save(GoodSpaceUserFixture.DEFAULT.getInstance());
    }

    @Nested
    class updatePassword {
        @Test
        @DisplayName("비밀번호를 변경한다")
        void changePassword() {
            // given
            String newPassword = "HelloNewPassword1!";
            PasswordUpdateRequestDto requestDto = PasswordUpdateRequestDto.builder()
                    .password(newPassword)
                    .build();

            // when
            userService.updatePassword(user.getId(), requestDto);

            // then
            assertThat(user.getPassword()).isEqualTo(newPassword);
        }
    }
}
