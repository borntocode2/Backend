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
public class Delivery {
    private String recipient;
    private String contactNumber1;
    private String contactNumber2;
    private Integer postalCode;
    private String address;
    private String detailedAddress;

    public static Delivery from(UserMyPageDto userMyPageDto){
        Delivery delivery=  new Delivery();

        delivery.recipient = userMyPageDto.getRecipient();
        delivery.contactNumber1 = userMyPageDto.getContactNumber1();
        delivery.contactNumber2 = userMyPageDto.getContactNumber2();
        delivery.postalCode = userMyPageDto.getPostalCode();
        delivery.address = userMyPageDto.getAddress();
        delivery.detailedAddress = userMyPageDto.getDetailedAddress();

        return delivery;
    }
}



