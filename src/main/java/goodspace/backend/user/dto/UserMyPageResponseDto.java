package goodspace.backend.user.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserMyPageResponseDto {
    private Integer dateOfBirth;
    private String email;
    private String name;
    private String phoneNumber;
    private String recipient;
    private String contactNumber1;
    private String contactNumber2;
    private String postalCode;
    private String address;
    private String detailedAddress;
}
