package goodspace.backend.order.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrdererInfo {
    private String email;
    private String name;
    private String phoneNumber;
}
