package goodspace.backend.dto;

public class AddressDto {
    private String postcode;
    private String address;
    private String detailAddress;
    private String extraAddress;

    // 기본 생성자
    public AddressDto() {}

    // getter / setter
    public String getPostcode() {
        return postcode;
    }
    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getDetailAddress() {
        return detailAddress;
    }
    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public String getExtraAddress() {
        return extraAddress;
    }
    public void setExtraAddress(String extraAddress) {
        this.extraAddress = extraAddress;
    }
}
