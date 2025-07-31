package goodspace.backend.user.dto;

import lombok.Builder;

@Builder
public class UserMyPageResponseDto {
    private Integer dateOfBirth;
    private String email;
    private String name;
    private String phoneNumber;
    private String recipient;
    private String contactNumber1;
    private String contactNumber2;
    private Integer postalCode;
    private String address;
    private String detailedAddress;
}
