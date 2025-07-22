package goodspace.backend.dto;

import goodspace.backend.domain.BaseEntity;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderRequestDto extends BaseEntity {
    private String orderId;
    private long userId;
    private List<ItemList> itemLists;
}
