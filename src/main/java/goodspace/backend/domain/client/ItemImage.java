package goodspace.backend.domain.client;

import jakarta.persistence.*;

@Entity
public class ItemImage {
    @Id
    @GeneratedValue
    private Long id;
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;
}
