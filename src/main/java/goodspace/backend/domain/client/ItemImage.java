package goodspace.backend.domain.client;

import goodspace.backend.domain.BaseEntity;
import jakarta.persistence.*;

@Entity
public class ItemImage extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;
}
