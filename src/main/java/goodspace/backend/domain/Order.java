package goodspace.backend.domain;

import goodspace.backend.domain.client.Item;
import goodspace.backend.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@SuperBuilder
@NoArgsConstructor
@Getter
public class Order extends BaseEntity {
    @Id
    @GeneratedValue
    private long id;

    private String orderId;

    @Embedded
    private PaymentApproveResult approveResult;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.PAYMENT_CONFIRMED;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order")
    private List<Item> items = new ArrayList<>();

    public void addItem(Item item) {
        items.add(item);
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
        this.approveResult = approveResult;
    }
}
