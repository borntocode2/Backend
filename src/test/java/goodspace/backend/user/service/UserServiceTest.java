package goodspace.backend.user.service;

import goodspace.backend.email.entity.EmailVerification;
import goodspace.backend.email.repository.EmailVerificationRepository;
import goodspace.backend.fixture.EmailVerificationFixture;
import goodspace.backend.fixture.GoodSpaceUserFixture;
import goodspace.backend.fixture.PaymentApproveResultFixture;
import goodspace.backend.global.parser.DateTimeParsers;
import goodspace.backend.order.domain.Order;
import goodspace.backend.order.domain.OrderCartItem;
import goodspace.backend.order.domain.PaymentApproveResult;
import goodspace.backend.order.repository.OrderRepository;
import goodspace.backend.user.domain.GoodSpaceUser;
import goodspace.backend.user.dto.*;
import goodspace.backend.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class UserServiceTest {
    static final Supplier<EntityNotFoundException> DTO_NOT_FOUND = () -> new EntityNotFoundException("DTO를 조회할 수 없습니다.");

    static final String DEFAULT_PASSWORD = "HelloPassword1!";
    static final String DEFAULT_REFRESH_TOKEN = "defaultRefreshToken";
    static final String NEW_PASSWORD = "HelloNewPassword1!";

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    EmailVerificationRepository emailVerificationRepository;
    @Autowired
    private OrderRepository orderRepository;

    GoodSpaceUser user;
    EmailVerification verifiedEmailOfUser;
    EmailVerification verifiedEmail;
    EmailVerification notVerifiedEmail;
    List<Order> existOrders;

    @BeforeEach
    void resetEntities() {
        user = userRepository.save(GoodSpaceUserFixture.DEFAULT.getInstance());
        user.updatePassword(passwordEncoder.encode(DEFAULT_PASSWORD));
        user.updateRefreshToken(DEFAULT_REFRESH_TOKEN);

        verifiedEmailOfUser = emailVerificationRepository.save(EmailVerificationFixture.DEFAULT.getInstance());
        verifiedEmailOfUser.verify();
        user.setEmail(verifiedEmailOfUser.getEmail());

        verifiedEmail = emailVerificationRepository.save(EmailVerificationFixture.VERIFIED.getInstance());
        notVerifiedEmail = emailVerificationRepository.save(EmailVerificationFixture.NOT_VERIFIED.getInstance());

        Order orderA = orderRepository.save(Order
                .builder()
                .user(user)
                .build());
        Order orderB = orderRepository.save(Order
                .builder()
                .user(user)
                .build());
        PaymentApproveResult approveResultA = PaymentApproveResultFixture.A.getInstanceWith(orderA.getId());
        PaymentApproveResult approveResultB = PaymentApproveResultFixture.B.getInstanceWith(orderB.getId());
        orderA.setPaymentApproveResult(approveResultA);
        orderB.setPaymentApproveResult(approveResultB);

        user.addOrder(orderA);
        user.addOrder(orderB);

        existOrders = List.of(orderA, orderB);
    }

    @Nested
    class getName {
        @Test
        @DisplayName("회원 이름을 조회한다")
        void getNameOfUser() {
            // given
            String expectedResult = user.getName();

            // when
            UserNameResponseDto actualResult = userService.getName(user.getId());

            // then
            assertThat(actualResult.name()).isEqualTo(expectedResult);
        }
    }

    @Nested
    class updatePassword {
        @Test
        @DisplayName("비밀번호를 변경한다")
        void changePassword() {
            // given
            PasswordUpdateRequestDto requestDto = PasswordUpdateRequestDto.builder()
                    .prevPassword(DEFAULT_PASSWORD)
                    .newPassword(NEW_PASSWORD)
                    .build();

            // when
            userService.updatePassword(user.getId(), requestDto);

            // then
            assertThat(isSamePassword(NEW_PASSWORD, user.getPassword())).isTrue();
        }

        @Test
        @DisplayName("리프레쉬 토큰을 새로 발급한다")
        void reissueRefreshToken() {
            // given
            PasswordUpdateRequestDto requestDto = PasswordUpdateRequestDto.builder()
                    .prevPassword(DEFAULT_PASSWORD)
                    .newPassword(NEW_PASSWORD)
                    .build();

            // when
            RefreshTokenResponseDto refreshTokenDto = userService.updatePassword(user.getId(), requestDto);

            // then
            assertThat(user.getRefreshToken()).isEqualTo(refreshTokenDto.refreshToken());
        }
    }

    @Nested
    class updatePasswordByVerifiedEmail {
        @Test
        @DisplayName("비밀번호를 변경한다")
        void changePassword() {
            // given
            PasswordUpdateByVerifiedEmailRequestDto requestDto = PasswordUpdateByVerifiedEmailRequestDto.builder()
                    .email(verifiedEmailOfUser.getEmail())
                    .password(NEW_PASSWORD)
                    .build();

            // when
            userService.updatePasswordByVerifiedEmail(requestDto);

            // then
            assertThat(isSamePassword(NEW_PASSWORD, user.getPassword())).isTrue();
        }

        @Test
        @DisplayName("리프레쉬 토큰을 새로 발급한다")
        void reissueRefreshToken() {
            // given
            PasswordUpdateByVerifiedEmailRequestDto requestDto = PasswordUpdateByVerifiedEmailRequestDto.builder()
                    .email(verifiedEmailOfUser.getEmail())
                    .password(NEW_PASSWORD)
                    .build();

            // when
            RefreshTokenResponseDto refreshTokenDto = userService.updatePasswordByVerifiedEmail(requestDto);

            // then
            assertThat(user.getRefreshToken()).isEqualTo(refreshTokenDto.refreshToken());
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

    @Nested
    class getPurchaseHistory {
        @Test
        @DisplayName("모든 주문 내역을 조회한다")
        void getEveryOrders() {
            List<PurchaseHistoryResponseDto> responseDtos = userService.getPurchaseHistory(user.getId());

            assertThat(responseDtos.size()).isEqualTo(existOrders.size());

            for (Order existOrder : existOrders) {
                PurchaseHistoryResponseDto responseDto = findDtoById(existOrder.getId(), responseDtos);
                assertThat(isEqual(existOrder, responseDto)).isTrue();
            }
        }
    }

    @Nested
    class removeUser {
        @Test
        @DisplayName("회원을 삭제한다")
        void removeUserById() {
            userService.removeUser(user.getId());

            assertThat(userRepository.findById(user.getId())).isEmpty();
        }
    }

    private boolean isSamePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    private PurchaseHistoryResponseDto findDtoById(long id, List<PurchaseHistoryResponseDto> dtos) {
        return dtos.stream()
                .filter(dto -> dto.id().equals(id))
                .findAny()
                .orElseThrow(DTO_NOT_FOUND);
    }

    private boolean isEqual(Order order, PurchaseHistoryResponseDto dto) {
        return Objects.equals(order.getId(), dto.id()) &&
                Objects.equals(order.getApproveResult().getGoodsName(), dto.itemInfo()) &&
                Objects.equals(getTotalQuantity(order.getOrderCartItems()), dto.totalQuantity()) &&
                Objects.equals(order.getApproveResult().getAmount(), dto.amount()) &&
                Objects.equals(order.getOrderStatus(), dto.status()) &&
                Objects.equals(DateTimeParsers.parseOffsetDateTime(order.getApproveResult().getPaidAt()), dto.date());
    }

    private int getTotalQuantity(List<OrderCartItem> orderCartItems) {
        return orderCartItems.stream()
                .mapToInt(OrderCartItem::getQuantity)
                .sum();
    }
}
