package goodspace.backend.admin.service.order;

import goodspace.backend.admin.dto.order.OrderInfoResponseDto;
import goodspace.backend.admin.dto.order.OrderUpdateRequestDto;
import goodspace.backend.admin.dto.order.TrackingNumberRegisterRequestDto;
import goodspace.backend.admin.dto.order.TrackingNumberUpdateRequestDto;
import goodspace.backend.order.domain.Order;
import goodspace.backend.order.domain.OrderStatus;
import goodspace.backend.order.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class OrderManageServiceImpl implements OrderManageService {
    private static final Supplier<EntityNotFoundException> ORDER_NOT_FOUND = () -> new EntityNotFoundException("주문을 찾을 수 없습니다.");
    private static final Supplier<IllegalStateException> ILLEGAL_ORDER_STATE = () -> new IllegalStateException("요청을 처리하기에 주문의 상태가 부적절합니다.");

    private final OrderRepository orderRepository;

    @Override
    @Transactional(readOnly = true)
    public List<OrderInfoResponseDto> getOrders() {
        return orderRepository.findAll().stream()
                .map(OrderInfoResponseDto::from)
                .toList();
    }

    @Override
    @Transactional
    public void acceptOrder(long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(ORDER_NOT_FOUND);

        if (order.getOrderStatus() != OrderStatus.PREPARING_PRODUCT) {
            throw ILLEGAL_ORDER_STATE.get();
        }

        order.updateOrderStatus(OrderStatus.MAKING_PRODUCT);
    }

    @Override
    @Transactional
    public void registerTrackingNumber(TrackingNumberRegisterRequestDto requestDto) {
        Order order = orderRepository.findById(requestDto.orderId())
                .orElseThrow(ORDER_NOT_FOUND);

        if (order.getOrderStatus() != OrderStatus.MAKING_PRODUCT) {
            throw ILLEGAL_ORDER_STATE.get();
        }

        order.setTrackingNumber(requestDto.trackingNumber());
        order.updateOrderStatus(OrderStatus.PREPARING_DELIVERY);
    }

    @Override
    @Transactional
    public void updateTrackingNumber(TrackingNumberUpdateRequestDto requestDto) {
        Order order = orderRepository.findById(requestDto.orderId())
                .orElseThrow(ORDER_NOT_FOUND);

        if (order.getOrderStatus() != OrderStatus.PREPARING_DELIVERY) {
            throw ILLEGAL_ORDER_STATE.get();
        }

        order.setTrackingNumber(requestDto.trackingNumber());
    }

    @Override
    @Transactional
    public void updateOrder(OrderUpdateRequestDto requestDto) {
        Order order = orderRepository.findById(requestDto.orderId())
                .orElseThrow(ORDER_NOT_FOUND);

        order.setPaymentApproveResult(requestDto.approveResult().toEntity());
        order.setDeliveryInfo(requestDto.deliveryInfo());
    }

    @Override
    @Transactional
    public void removeOrder(long orderId) {
        orderRepository.deleteById(orderId);
    }
}
