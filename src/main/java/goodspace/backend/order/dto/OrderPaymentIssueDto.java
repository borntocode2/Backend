package goodspace.backend.order.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderPaymentIssueDto {
    private Long orderId;
    private Long tid;
}
