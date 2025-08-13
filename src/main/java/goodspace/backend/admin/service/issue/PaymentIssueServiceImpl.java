package goodspace.backend.admin.service.issue;

import goodspace.backend.admin.dto.order.PaymentIssueResponseDto;
import goodspace.backend.order.domain.OrderPaymentIssue;
import goodspace.backend.order.repository.OrderPaymentIssueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentIssueServiceImpl implements PaymentIssueService {
    private final OrderPaymentIssueRepository orderPaymentIssueRepository;

    @Override
    @Transactional(readOnly = true)
    public List<PaymentIssueResponseDto> find() {
        return orderPaymentIssueRepository.findAll().stream()
                .map(PaymentIssueResponseDto::from)
                .toList();
    }

    @Override
    @Transactional
    public void create(Long orderId, Long tid){
        OrderPaymentIssue orderPaymentIssue = OrderPaymentIssue.builder()
                .orderId(orderId)
                .tid(tid)
                .build();

        orderPaymentIssueRepository.save(orderPaymentIssue);
    }

    @Override
    @Transactional
    public void remove(Long issueId) {
        orderPaymentIssueRepository.deleteById(issueId);
    }
}
