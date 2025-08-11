package goodspace.backend.delivery.domain;

import goodspace.backend.order.domain.Order;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class DeliveryStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private String sender;
    private String receiver;
    private String completeDate;
    private String status;
    private String postType;
    private String registrationNumber;
    private String treatType;
}
