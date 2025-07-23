package goodspace.backend.domain.client;

import goodspace.backend.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ItemImage extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "item_id")
    @Setter
    private Item item;
}
