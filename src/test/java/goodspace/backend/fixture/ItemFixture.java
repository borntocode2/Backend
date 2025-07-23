package goodspace.backend.fixture;

import goodspace.backend.domain.client.Item;
import goodspace.backend.domain.client.ItemImage;

import java.util.List;

public enum ItemFixture {
    A(
            "ITEM_A",
            15000,
            "short description of Item A",
            "landing page description of Item A",
            List.of("http://item-a-image-1", "http://item-a-image-2")
    ),
    B(
            "ITEM_B",
            20000,
            "short description of Item B",
            "landing page description of Item B",
            List.of("http://item-b-image-1", "http://item-b-image-2", "http://item-b-image-3")
    ),
    C(
            "ITEM_C",
            30000,
            "short description of Item C",
            "landing page description of Item C",
            List.of("http://item-c-image-1", "http://item-c-image-2", "http://item-c-image-3", "http://item-c-image-4")
    );

    private final String name;
    private final Integer price;
    private final String shortDescription;
    private final String landingPageDescription;
    private final List<String> imageUrls;

    ItemFixture(
            String name,
            Integer price,
            String shortDescription,
            String landingPageDescription,
            List<String> imageUrls
    ) {
        this.name = name;
        this.price = price;
        this.shortDescription = shortDescription;
        this.landingPageDescription = landingPageDescription;
        this.imageUrls = imageUrls;
    }

    public Item getInstance() {
        Item item = Item.builder()
                .name(name)
                .price(price)
                .shortDescription(shortDescription)
                .landingPageDescription(landingPageDescription)
                .build();

        List<ItemImage> itemImages = imageUrls.stream()
                .map(this::createItemImageFromUrl)
                .toList();

        item.addItemImages(itemImages);

        return item;
    }

    private ItemImage createItemImageFromUrl(String url) {
        return ItemImage.builder()
                .imageUrl(url)
                .build();
    }
}
