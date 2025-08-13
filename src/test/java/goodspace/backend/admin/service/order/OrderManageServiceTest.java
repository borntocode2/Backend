package goodspace.backend.admin.service.order;

import goodspace.backend.admin.dto.order.OrderInfoResponseDto;
import goodspace.backend.admin.dto.order.OrderUpdateRequestDto;
import goodspace.backend.admin.dto.order.PaymentApproveResultDto;
import goodspace.backend.admin.dto.order.TrackingNumberRegisterRequestDto;
import goodspace.backend.fixture.DeliveryFixture;
import goodspace.backend.fixture.GoodSpaceUserFixture;
import goodspace.backend.fixture.PaymentApproveResultFixture;
import goodspace.backend.order.domain.Order;
import goodspace.backend.order.domain.PaymentApproveResult;
import goodspace.backend.order.repository.OrderRepository;
import goodspace.backend.user.domain.DeliveryInfo;
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

import static goodspace.backend.order.domain.OrderStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class OrderManageServiceTest {
    static final Supplier<EntityNotFoundException> DTO_NOT_FOUND = () -> new EntityNotFoundException("DTO를 찾을 수 없습니다.");
    static final DeliveryInfo DEFAULT_DELIVERY = DeliveryFixture.A.getInstance();
    static final DeliveryInfo NEW_DELIVERY = DeliveryFixture.B.getInstance();
    static final PaymentApproveResultFixture DEFAULT_PAYMENT_APPROVE_RESULT_FIXTURE = PaymentApproveResultFixture.A;
    static final PaymentApproveResultFixture NEW_PAYMENT_APPROVE_RESULT_FIXTURE = PaymentApproveResultFixture.B;
    static final String TRACKING_NUMBER = "12345";

    @Autowired
    OrderManageService orderManageService;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UserRepository userRepository;

    Order order;
    Order preparingProductOrder;
    Order makingProductOrder;

    List<Order> existOrders;

    @BeforeEach
    void resetEntities() {
        User user = userRepository.save(GoodSpaceUserFixture.DEFAULT.getInstance());

        order = orderRepository.save(Order.builder()
                .deliveryInfo(DEFAULT_DELIVERY)
                .user(user)
                .build());
        order.setPaymentApproveResult(DEFAULT_PAYMENT_APPROVE_RESULT_FIXTURE.getInstanceWith(order.getId()));

        preparingProductOrder = orderRepository.save(Order.builder()
                .deliveryInfo(DEFAULT_DELIVERY)
                .orderStatus(PREPARING_PRODUCT)
                .user(user)
                .build());
        preparingProductOrder.setPaymentApproveResult(DEFAULT_PAYMENT_APPROVE_RESULT_FIXTURE.getInstanceWith(preparingProductOrder.getId()));

        makingProductOrder = orderRepository.save(Order.builder()
                .deliveryInfo(DEFAULT_DELIVERY)
                .orderStatus(MAKING_PRODUCT)
                .user(user)
                .build());
        makingProductOrder.setPaymentApproveResult(DEFAULT_PAYMENT_APPROVE_RESULT_FIXTURE.getInstanceWith(makingProductOrder.getId()));

        user.addOrders(order, preparingProductOrder, makingProductOrder);
        existOrders = List.of(order, preparingProductOrder, makingProductOrder);
    }

    @Nested
    class getOrders {
        @Test
        @DisplayName("모든 주문을 조회한다")
        void getEveryOrder() {
            List<OrderInfoResponseDto> orderDtos = orderManageService.getOrders();

            assertThat(orderDtos.size()).isEqualTo(existOrders.size());

            for (Order existOrder : existOrders) {
                OrderInfoResponseDto orderDto = findDtoById(existOrder.getId(), orderDtos);

                assertThat(isEqualWithoutItem(existOrder, orderDto)).isTrue();
            }
        }
    }

    @Nested
    class acceptOrder {
        @Test
        @DisplayName("주문의 상태를 '제작중'으로 전이한다")
        void changeOrderStatusToMakingProduct() {
            orderManageService.acceptOrder(preparingProductOrder.getId());

            assertThat(preparingProductOrder.getOrderStatus()).isSameAs(MAKING_PRODUCT);
        }

        @Test
        @DisplayName("주문의 상태가 '제작 준비중'이 아니라면 예외가 발생한다")
        void ifOrderStatusIsNotPaymentCheckingThenThrowException() {
            assertThatThrownBy(() -> orderManageService.acceptOrder(makingProductOrder.getId()))
                    .isInstanceOf(IllegalStateException.class);
        }
    }

    @Nested
    class registerTrackingNumber {
        @Test
        @DisplayName("주문 엔티티에 등기 번호를 등록한다")
        void registerTrackingNumberToOrder() {
            // given
            TrackingNumberRegisterRequestDto requestDto = TrackingNumberRegisterRequestDto.builder()
                    .orderId(makingProductOrder.getId())
                    .trackingNumber(TRACKING_NUMBER)
                    .build();

            // when
            orderManageService.registerTrackingNumber(requestDto);

            // then
            assertThat(makingProductOrder.getTrackingNumber()).isEqualTo(TRACKING_NUMBER);
        }

        @Test
        @DisplayName("주문의 상태를 '배송 준비중'으로 전이한다")
        void changeOrderStatusToShipping() {
            // given
            TrackingNumberRegisterRequestDto requestDto = TrackingNumberRegisterRequestDto.builder()
                    .orderId(makingProductOrder.getId())
                    .trackingNumber(TRACKING_NUMBER)
                    .build();

            // when
            orderManageService.registerTrackingNumber(requestDto);

            // then
            assertThat(makingProductOrder.getOrderStatus()).isEqualTo(PREPARING_DELIVERY);
        }

        @Test
        @DisplayName("주문의 상태가 '제작중'이 아니라면 예외가 발생한다")
        void ifOrderStatusIsNotMakingProductThenThrowException() {
            TrackingNumberRegisterRequestDto requestDto = TrackingNumberRegisterRequestDto.builder()
                    .orderId(preparingProductOrder.getId())
                    .trackingNumber(TRACKING_NUMBER)
                    .build();

            assertThatThrownBy(() -> orderManageService.registerTrackingNumber(requestDto))
                    .isInstanceOf(IllegalStateException.class);
        }
    }

    @Nested
    class updateOrder {
        @Test
        @DisplayName("주문 정보를 수정한다")
        void updateOrderInfo() {
            // given
            PaymentApproveResult newApproveResult = NEW_PAYMENT_APPROVE_RESULT_FIXTURE.getInstanceWith(order.getId());

            OrderUpdateRequestDto requestDto = OrderUpdateRequestDto.builder()
                    .orderId(order.getId())
                    .approveResult(PaymentApproveResultDto.from(newApproveResult))
                    .deliveryInfo(NEW_DELIVERY)
                    .build();

            // when
            orderManageService.updateOrder(requestDto);

            // then
            assertThat(order.getApproveResult()).isEqualTo(newApproveResult);
            assertThat(order.getDeliveryInfo()).isEqualTo(NEW_DELIVERY);
        }
    }

    @Nested
    class removeOrder {
        @Test
        @DisplayName("주문을 제거한다")
        void deleteOrder() {
            orderManageService.removeOrder(order.getId());

            assertThat(orderRepository.findById(order.getId())).isEmpty();
        }
    }

    private OrderInfoResponseDto findDtoById(long id, List<OrderInfoResponseDto> dtos) {
        return dtos.stream()
                .filter(dto -> dto.id().equals(id))
                .findAny()
                .orElseThrow(DTO_NOT_FOUND);
    }

    private boolean isEqualWithoutItem(Order order, OrderInfoResponseDto dto) {
        return Objects.equals(order.getId(), dto.id()) &&
                isEqual(order.getApproveResult(), dto.approveResult()) &&
                Objects.equals(order.getDeliveryInfo(), dto.deliveryInfo()) &&
                Objects.equals(order.getOrderStatus(), dto.status()) &&
                Objects.equals(order.getCreatedAt(), dto.createAt()) &&
                Objects.equals(order.getUpdatedAt(), dto.updatedAt());
    }

    private boolean isEqual(PaymentApproveResult approveResult, PaymentApproveResultDto dto) {
        if (approveResult == null && dto == null) return true;
        if (approveResult == null || dto == null) return false;

        PaymentApproveResultDto converted = PaymentApproveResultDto.from(approveResult);
        return Objects.equals(converted, dto);
    }
}
