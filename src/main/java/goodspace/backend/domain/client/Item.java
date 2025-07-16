package goodspace.backend.domain.client;

import goodspace.backend.domain.Order;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Item {
    @Id
    private Long id;
    private String name;
    private Integer price;
    private String detail;
    private String shortDescription;
    private String sizeDescription;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

}
