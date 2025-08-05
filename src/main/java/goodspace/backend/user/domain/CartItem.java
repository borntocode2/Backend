package goodspace.backend.user.domain;

import goodspace.backend.global.domain.BaseEntity;
import goodspace.backend.global.domain.Item;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CartItem extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "item_id")
    @Setter
    private Item item;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @Setter
    private User user;

    public void updateQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
