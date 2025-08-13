package goodspace.backend.admin.service.issue;

import goodspace.backend.admin.dto.order.PaymentIssueResponseDto;

import java.util.List;

public interface PaymentIssueService {
    List<PaymentIssueResponseDto> find();

    void create(Long orderId, Long tid);

    void remove(Long issueId);
}
