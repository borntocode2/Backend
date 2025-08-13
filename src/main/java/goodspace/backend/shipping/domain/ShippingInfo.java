package goodspace.backend.shipping.domain;

import goodspace.backend.global.domain.BaseEntity;
import goodspace.backend.order.domain.Order;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Entity
@Table(name = "shipping_info")
@NoArgsConstructor
@SuperBuilder
public class ShippingInfo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String trackingNumber;
    private String senderName;
    private String senderDate;          // senderData
    private String receiverName;
    private String receiverDate;
    private String trackState;          // 배송 상태
    private String expressType;

    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Embedded
    private ShippingDetail detail;

    public void updateDetail(ShippingDetail detail){
        if(this.detail != null) {
            this.detail = detail;
        }
    }
}
