package goodspace.backend.global.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemImage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "item_id")
    @Setter
    private Item item;

    public static ItemImage from(String imageUrl) {
        return ItemImage.builder()
                .imageUrl(imageUrl)
                .build();
    }

    public static ItemImage getEmptyInstance() {
        return new ItemImage();
    }
}
