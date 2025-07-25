package goodspace.backend.order.service;

import goodspace.backend.order.domain.Order;
import goodspace.backend.order.domain.PaymentApproveResult;
import goodspace.backend.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NicePayService {
    private final OrderRepository orderRepository;

    public void MappingOrderWithPaymentApproveResult(PaymentApproveResult approveResult) {
        System.out.println("OrderID는 : " + approveResult.getOrderId());
        Order order = orderRepository.findByOrderOutId(approveResult.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("결제 성공 후, 결제API에서 응답받은 orderId로 orderRepository에 해당 order를 찾을 수 없습니다."));

        order.setPaymentApproveResult(approveResult);
    }
}
