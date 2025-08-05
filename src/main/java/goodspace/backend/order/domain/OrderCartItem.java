package goodspace.backend.order.domain;

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
public class OrderCartItem {
    @Id
    @GeneratedValue
    private Long id;

    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;



    public void setOrder(Order order) {
        this.order = order;
    }
}
