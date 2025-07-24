package goodspace.backend.domain;

import goodspace.backend.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@SuperBuilder
@NoArgsConstructor
@Getter
@Table(name = "`order`")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue
    private long id;

    @Embedded
    private PaymentApproveResult approveResult;

    private String orderOutId;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.PAYMENT_CONFIRMED;



    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderCartItem> orderCartItems = new ArrayList<>();


    public void setOrderCartItems(List<OrderCartItem> cartItems) {
        this.orderCartItems = cartItems;
        for (OrderCartItem cartItem : cartItems) {
            cartItem.setOrder(this); // 양방향 관계 유지
        }
    }

    public void updateOrderStatus(String status){
        if(status.equals("결제 확인")){
            this.orderStatus = OrderStatus.PAYMENT_CONFIRMED;
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

    public void setPaymentApproveResult(PaymentApproveResult approveResult) {
        if (this.orderOutId == approveResult.getOrderId())
        {
            this.approveResult = approveResult;
        }
    }
}
