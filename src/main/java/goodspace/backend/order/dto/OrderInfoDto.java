package goodspace.backend.order.dto;

import goodspace.backend.user.domain.DeliveryInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderInfoDto {
    private String email;
    private String name;
    private String phoneNumber;

    private String recipient;
    private String contactNumber1;
    private String contactNumber2;
    private String postalCode;
    private String address;
    private String detailedAddress;

    public DeliveryInfo toDeliveryInfo() {
        return DeliveryInfo.builder()
                .recipient(recipient)
                .contactNumber1(contactNumber1)
                .contactNumber2(contactNumber2)
                .postalCode(postalCode)
                .address(address)
                .detailedAddress(detailedAddress)
                .build();
    }
}
