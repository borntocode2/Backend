package goodspace.backend.payment.service;

import com.fasterxml.jackson.databind.JsonNode;
import goodspace.backend.domain.Order;
import goodspace.backend.domain.PaymentApproveResult;
import goodspace.backend.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class NicePayService {
    private OrderRepository orderRepository;

    public void MappingOrderWithPaymentApproveResult(PaymentApproveResult approveResult) {
        Order order = orderRepository.findByOrderId(approveResult.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("결제 성공 후, 결제API에서 응답받은 orderId로 orderRepository에 해당 order를 찾을 수 없습니다."));

        order.setPaymentApproveResult(approveResult);
    }
}
