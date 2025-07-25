package goodspace.backend.order.domain;

public enum OrderStatus {
    PAYMENT_CONFIRMED,   // 결제 확인
    PREPARING_PRODUCT,   // 제작 준비중
    MAKING_PRODUCT,      // 제작 중
    PREPARING_DELIVERY,  // 배송 준비중
    SHIPPING,            // 배송중
    DELIVERED,            // 배송완료
    CANCELED
}
