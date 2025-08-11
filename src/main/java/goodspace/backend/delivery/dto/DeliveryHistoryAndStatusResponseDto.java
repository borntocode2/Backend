package goodspace.backend.delivery.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class DeliveryHistoryAndStatusResponseDto {
    private DeliveryStatusDto status;
    private List<DeliveryHistoryDto> historys;
}
