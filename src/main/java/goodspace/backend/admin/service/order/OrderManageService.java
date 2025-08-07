package goodspace.backend.admin.service.order;

import goodspace.backend.admin.dto.order.OrderInfoResponseDto;
import goodspace.backend.admin.dto.order.OrderUpdateRequestDto;
import goodspace.backend.admin.dto.order.TrackingNumberRegisterRequestDto;

import java.util.List;

public interface OrderManageService {
    List<OrderInfoResponseDto> getOrders();

    void acceptOrder(long orderId);

    void registerTrackingNumber(TrackingNumberRegisterRequestDto requestDto);

    void updateOrder(OrderUpdateRequestDto requestDto);

    void removeOrder(long orderId);
}
