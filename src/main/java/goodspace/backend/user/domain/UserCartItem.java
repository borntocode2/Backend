package goodspace.backend.user.domain;

import goodspace.backend.global.domain.BaseEntity;
import goodspace.backend.global.domain.Item;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UserCartItem extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
