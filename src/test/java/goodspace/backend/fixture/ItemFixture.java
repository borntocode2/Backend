package goodspace.backend.fixture;

import goodspace.backend.global.domain.Item;

public enum ItemFixture {
    A(
            "ITEM_A",
            15000,
            "short description of Item A",
            "landing page description of Item A"
    ),
    B(
            "ITEM_B",
            20000,
            "short description of Item B",
            "landing page description of Item B"
    ),
    C(
            "ITEM_C",
            30000,
            "short description of Item C",
            "landing page description of Item C"
    ),
    D(
            "ITEM_D",
            40000,
            "short description of Item D",
            "landing page description of Item D"
    );

    private final String name;
    private final Integer price;
    private final String shortDescription;
    private final String landingPageDescription;

    ItemFixture(
            String name,
            Integer price,
            String shortDescription,
            String landingPageDescription
    ) {
        this.name = name;
        this.price = price;
        this.shortDescription = shortDescription;
        this.landingPageDescription = landingPageDescription;
    }

    public Item getInstance() {
        return Item.builder()
                .name(name)
                .price(price)
                .shortDescription(shortDescription)
                .landingPageDescription(landingPageDescription)
                .build();
    }
}
