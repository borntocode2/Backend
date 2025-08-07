package goodspace.backend.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserMyPageDto {
    private Integer dateOfBirth;
    private String name;
    private String phoneNumber;
    private String recipient;
    private String contactNumber1;
    private String contactNumber2;
    private String postalCode;
    private String address;
    private String detailedAddress;
}
