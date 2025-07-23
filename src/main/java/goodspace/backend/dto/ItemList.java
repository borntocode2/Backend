package goodspace.backend.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ItemList {
    private long itemId;
    private long quantity;
    private long price;
}
