package goodspace.backend.order.domain;

import goodspace.backend.global.domain.BaseEntity;
import goodspace.backend.user.domain.DeliveryInfo;
import goodspace.backend.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@SuperBuilder
@NoArgsConstructor
@Getter
@Table(name = "`order`")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private PaymentApproveResult approveResult;

    @Setter
    @Embedded
    private DeliveryInfo deliveryInfo;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private OrderStatus orderStatus = OrderStatus.PAYMENT_CHECKING;

    @Setter
    private String trackingNumber;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @Setter
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderCartItem> orderCartItems = new ArrayList<>();

    public void setOrderCartItems(List<OrderCartItem> cartItems) {
        this.orderCartItems = cartItems;
        for (OrderCartItem cartItem : cartItems) {
            cartItem.setOrder(this);
        }
    }

    public void updateOrderStatus(String status){
        if(status.equals("결제 확인")){
            this.orderStatus = OrderStatus.PAYMENT_CHECKING;
        }
        else if(status.equals("제작 준비중")){
            this.orderStatus = OrderStatus.PREPARING_PRODUCT;
        }
        else if(status.equals("제작 중")){
            this.orderStatus = OrderStatus.MAKING_PRODUCT;
        }
        else if(status.equals("배송 준비중")){
            this.orderStatus = OrderStatus.PREPARING_DELIVERY;
        }
        else if (status.equals("배송 중")){
            this.orderStatus = OrderStatus.SHIPPING;
        }
        else if (status.equals("배송완료")){
            this.orderStatus = OrderStatus.DELIVERED;
        }
        else if (status.equals("취소")){
            this.orderStatus = OrderStatus.CANCELED;
        }
    }

    public void updateOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void setPaymentApproveResult(PaymentApproveResult approveResult) {
        if (Objects.equals(this.id, approveResult.getOrderId()))
        {
            this.approveResult = approveResult;
        }
    }
}
