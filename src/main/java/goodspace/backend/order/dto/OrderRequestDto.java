package goodspace.backend.order.dto;

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
    private boolean requireUpdateUserInfo;
    private boolean requireCartItemRemove;
}
