package goodspace.backend.order.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class PaymentVerifyRequestDto {
    private String authResultCode;
    private String authResultMsg;
    private String tid;
    private String clientId;
    private String orderId;
    private String amount;
    private String mallReserved;
    private String authToken;
    private String signature;
}
