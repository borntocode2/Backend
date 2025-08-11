package goodspace.backend.order.service;

import goodspace.backend.order.domain.Order;
import goodspace.backend.order.domain.PaymentApproveResult;
import goodspace.backend.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class NicePayService {
    private final OrderRepository orderRepository;

    public void MappingOrderWithPaymentApproveResult(PaymentApproveResult approveResult) {
        try {
            Order order = orderRepository.findById(approveResult.getOrderId())
                    .orElseThrow(() -> new IllegalArgumentException("결제 성공 후, 결제API에서 응답받은 orderId로 orderRepository에 해당 order를 찾을 수 없습니다."));

            order.setPaymentApproveResult(approveResult);

        } catch (IllegalArgumentException e) {
            log.error("[error 단계 에러]에러 발생", e);

        } catch (Exception e) {
            log.info("[info 단계 에러] 예기치 못한 오류: ", e.getMessage());
        }
    }
}
