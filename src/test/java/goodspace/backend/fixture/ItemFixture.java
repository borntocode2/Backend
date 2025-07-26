package goodspace.backend.fixture;

import goodspace.backend.client.domain.RegisterStatus;
import goodspace.backend.global.domain.Item;

public enum ItemFixture {
    PUBLIC_A(
            "PUBLIC_A",
            15000,
            "short description of Item CA",
            "landing page description of Item CA",
            RegisterStatus.PUBLIC
    ),
    PUBLIC_B(
            "PUBLIC_B",
            20000,
            "short description of Item CB",
            "landing page description of Item CB",
            RegisterStatus.PUBLIC
    ),
    PUBLIC_C(
            "PUBLIC_C",
            30000,
            "short description of Item CC",
            "landing page description of Item CC",
            RegisterStatus.PUBLIC
    ),
    PRIVATE_A(
            "PRIVATE_A",
            40000,
            "short description of Item TA",
            "landing page description of Item TA",
            RegisterStatus.PRIVATE
    ),
    PRIVATE_B(
            "PRIVATE_B",
            40000,
            "short description of Item TB",
            "landing page description of Item TB",
            RegisterStatus.PRIVATE
    ),
    PRIVATE_C(
            "PRIVATE_C",
            40000,
            "short description of Item TC",
            "landing page description of Item TC",
            RegisterStatus.PRIVATE
    );

    private final String name;
    private final Integer price;
    private final String shortDescription;
    private final String landingPageDescription;
    private final RegisterStatus status;

    ItemFixture(
            String name,
            Integer price,
            String shortDescription,
            String landingPageDescription,
            RegisterStatus status
    ) {
        this.name = name;
        this.price = price;
        this.shortDescription = shortDescription;
        this.landingPageDescription = landingPageDescription;
        this.status = status;
    }

    public Item getInstance() {
        return Item.builder()
                .name(name)
                .price(price)
                .shortDescription(shortDescription)
                .landingPageDescription(landingPageDescription)
                .status(status)
                .build();
    }
}
