package goodspace.backend.order.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderCreateResponseDto {
    //TODO: 아이템명 추가
    Long orderId;
}
