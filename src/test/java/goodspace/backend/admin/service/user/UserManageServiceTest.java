package goodspace.backend.admin.service.user;

import goodspace.backend.admin.dto.user.UserInfoDto;
import goodspace.backend.fixture.GoodSpaceUserFixture;
import goodspace.backend.user.domain.GoodSpaceUser;
import goodspace.backend.user.domain.OAuthType;
import goodspace.backend.user.domain.OAuthUser;
import goodspace.backend.user.domain.User;
import goodspace.backend.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UserManageServiceTest {
    static final Supplier<EntityNotFoundException> DTO_NOT_FOUND = () -> new EntityNotFoundException("DTO를 조회할 수 없습니다.");

    @Autowired
    UserManageService userManageService;
    @Autowired
    UserRepository userRepository;

    User existUser;
    List<User> existUsers;

    @BeforeEach
    void resetEntities() {
        // 자동으로 만들어지는 관리자 계정 제거
        userRepository.deleteAll();

        existUser = userRepository.save(GoodSpaceUserFixture.DEFAULT.getInstance());
        User existUserA = userRepository.save(GoodSpaceUserFixture.A.getInstance());
        User existUserB = userRepository.save(GoodSpaceUserFixture.B.getInstance());

        existUsers = List.of(existUser, existUserA, existUserB);
    }

    @Nested
    class getUsers {
        @Test
        @DisplayName("모든 회원을 조회한다")
        void findEveryUsers() {
            List<UserInfoDto> userDtos = userManageService.getUsers();

            assertThat(userDtos.size()).isEqualTo(existUsers.size());

            for (User existUser : existUsers) {
                UserInfoDto userDto = findDtoById(existUser.getId(), userDtos);

                assertThat(isEqual(existUser, userDto)).isTrue();
            }
        }
    }

    @Nested
    class removeUser {
        @Test
        @DisplayName("회원을 삭제한다")
        void deleteUser() {
            userManageService.removeUser(existUser.getId());

            assertThat(userRepository.findById(existUser.getId())).isEmpty();
        }
    }

    private UserInfoDto findDtoById(long id, List<UserInfoDto> dtos) {
        return dtos.stream()
                .filter(dto -> dto.id().equals(id))
                .findAny()
                .orElseThrow(DTO_NOT_FOUND);
    }

    private boolean isEqual(User user, UserInfoDto dto) {
        if (user instanceof GoodSpaceUser && dto.oauthType() != OAuthType.GOOD_SPACE) {
            return false;
        }
        if (user instanceof OAuthUser oAuthUser && oAuthUser.getOauthType() != dto.oauthType()) {
            return false;
        }

        return Objects.equals(user.getId(), dto.id()) &&
                Objects.equals(user.getName(), dto.name()) &&
                Objects.equals(user.getDateOfBirth(), dto.dateOfBirth()) &&
                Objects.equals(user.getEmail(), dto.email()) &&
                Objects.equals(user.getPhoneNumber(), dto.phoneNumber()) &&
                Objects.equals(user.getRoles(), dto.roles()) &&
                Objects.equals(user.getDeliveryInfo(), dto.deliveryInfo());
    }
}
