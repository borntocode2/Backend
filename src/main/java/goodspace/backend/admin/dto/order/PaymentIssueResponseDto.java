package goodspace.backend.admin.dto.order;

import goodspace.backend.order.domain.OrderPaymentIssue;
import lombok.Builder;

@Builder
public record PaymentIssueResponseDto(
    Long issueId,
    Long orderId,
    Long tid
) {
    public static PaymentIssueResponseDto from(OrderPaymentIssue orderPaymentIssue) {
        return PaymentIssueResponseDto.builder()
                .issueId(orderPaymentIssue.getId())
                .orderId(orderPaymentIssue.getOrderId())
                .tid(orderPaymentIssue.getTid())
                .build();
    }
}
