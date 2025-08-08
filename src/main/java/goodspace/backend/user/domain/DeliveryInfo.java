package goodspace.backend.user.domain;


import goodspace.backend.user.dto.UserMyPageDto;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryInfo {
    private String recipient;
    private String contactNumber1;
    private String contactNumber2;
    private String postalCode;
    private String address;
    private String detailedAddress;

    public static DeliveryInfo from(UserMyPageDto userMyPageDto){
        DeliveryInfo deliveryInfo =  new DeliveryInfo();

        deliveryInfo.recipient = userMyPageDto.getRecipient();
        deliveryInfo.contactNumber1 = userMyPageDto.getContactNumber1();
        deliveryInfo.contactNumber2 = userMyPageDto.getContactNumber2();
        deliveryInfo.postalCode = userMyPageDto.getPostalCode();
        deliveryInfo.address = userMyPageDto.getAddress();
        deliveryInfo.detailedAddress = userMyPageDto.getDetailedAddress();

        return deliveryInfo;
    }
}



