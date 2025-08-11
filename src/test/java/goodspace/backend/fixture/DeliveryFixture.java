package goodspace.backend.fixture;

import goodspace.backend.user.domain.DeliveryInfo;

public enum DeliveryFixture {
    A(
        "A Recipient",
            "01012345678",
            "01011112222",
            "10101",
            "A Address",
            "A Detailed Address"
    ),
    B(
            "B Recipient",
            "01023456789",
            "01022222222",
            "20202",
            "B Address",
            "B Detailed Address"
    );

    private final String recipient;
    private final String contactNumber1;
    private final String contactNumber2;
    private final String postalCode;
    private final String address;
    private final String detailedAddress;

    DeliveryFixture(
            String recipient,
            String contactNumber1,
            String contactNumber2,
            String postalCode,
            String address,
            String detailedAddress
    ) {
        this.recipient = recipient;
        this.contactNumber1 = contactNumber1;
        this.contactNumber2 = contactNumber2;
        this.postalCode = postalCode;
        this.address = address;
        this.detailedAddress = detailedAddress;
    }

    public DeliveryInfo getInstance() {
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
