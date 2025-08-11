package goodspace.backend.cart.domain;

import goodspace.backend.global.domain.BaseEntity;
import goodspace.backend.global.domain.Item;
import goodspace.backend.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@SQLDelete(sql = "UPDATE cart_item SET deleted = true, deleted_at = NOW() WHERE id = ?")
public class CartItem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    @Setter
    private Item item;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @Setter
    private User user;

    public void updateQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
