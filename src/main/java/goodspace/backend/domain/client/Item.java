package goodspace.backend.domain.client;

import goodspace.backend.domain.BaseEntity;
import goodspace.backend.domain.Order;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Item extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer price;
    private String shortDescription;
    private Long quantity;

    private String landingPageDescription;

    @ManyToOne
    @JoinColumn(name = "client_id")
    @Setter
    private Client client;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private final List<ItemImage> itemImages = new ArrayList<>();

    public List<String> getImageUrls() {
        return itemImages.stream()
                .map(ItemImage::getImageUrl)
                .toList();
    }

    public void update(
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

    /**
     * Item - ItemImage 연관관계 편의 메서드
     */
    public void addItemImages(List<ItemImage> itemImages) {
        for (ItemImage itemImage : itemImages) {
            this.itemImages.add(itemImage);
            itemImage.setItem(this);
        }
    }

    public void removeItemImage(ItemImage itemImage) {
        itemImages.remove(itemImage);
        itemImage.setItem(null);
    }

    public void removeEveryImages() {
        itemImages.clear();
    }
}
