package goodspace.backend.order.dto;

import goodspace.backend.global.domain.BaseEntity;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderRequestDto extends BaseEntity {
    private String orderOutId;
    private long userId;
    private List<OrderCartItemDto> orderCartItemDtos;
}
