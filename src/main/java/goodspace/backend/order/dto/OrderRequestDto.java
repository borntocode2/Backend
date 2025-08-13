package goodspace.backend.order.dto;

import goodspace.backend.user.domain.DeliveryInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDto {
    private List<OrderCartItemDto> orderCartItemDtos;
    private OrderInfoDto orderInfo;
    private Boolean requireUpdateUserInfo;

    public DeliveryInfo getDeliveryInfo() {
        return orderInfo.toDeliveryInfo();
    }
}
