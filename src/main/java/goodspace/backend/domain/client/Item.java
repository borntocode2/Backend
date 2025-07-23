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
    private Long id;
    private String name;
    private Integer price;
    private String shortDescription;
    private String landingPageDescription;

    @ManyToOne
    @JoinColumn(name = "client_id")
    @Setter
    private Client client;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @Setter
    private Order order;

    @OneToMany(mappedBy = "item")
    @Builder.Default
    private final List<ItemImage> itemImages = new ArrayList<>();

    public List<String> getImageUrls() {
        return itemImages.stream()
                .map(ItemImage::getImageUrl)
                .toList();
    }

    public void addItemImages(List<ItemImage> itemImages) {
        for (ItemImage itemImage : itemImages) {
            this.itemImages.add(itemImage);
            itemImage.setItem(this);
        }
    }
}
