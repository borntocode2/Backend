package goodspace.backend.domain.client;

import goodspace.backend.domain.BaseEntity;
import goodspace.backend.domain.Order;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.checkerframework.checker.units.qual.N;

@Entity
@Getter
public class Item extends BaseEntity {
    @Id
    private Long id;
    private String name;
    private Long price;
    private String shortDescription;
    private String randingPageDescription;
    private Long quantity;


    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;



}
