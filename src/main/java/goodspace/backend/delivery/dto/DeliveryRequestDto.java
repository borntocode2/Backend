package goodspace.backend.delivery.dto;

import lombok.Getter;

@Getter
public class DeliveryRequestDto {
    private Long orderId;
    private String registrationNumber;
}
