package goodspace.backend.order.dto;

import goodspace.backend.global.domain.BaseEntity;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderRequestDto {
    private List<OrderCartItemDto> orderCartItemDtos;
}
