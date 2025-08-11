package goodspace.backend.admin.service.order;

import goodspace.backend.admin.dto.order.OrderInfoResponseDto;
import goodspace.backend.admin.dto.order.OrderUpdateRequestDto;
import goodspace.backend.admin.dto.order.TrackingNumberRegisterRequestDto;
import goodspace.backend.order.dto.OrderPaymentIssueDto;

import java.util.List;

public interface OrderManageService {
    List<OrderInfoResponseDto> getOrders();

    void acceptOrder(long orderId);

    void registerTrackingNumber(TrackingNumberRegisterRequestDto requestDto);

    void updateOrder(OrderUpdateRequestDto requestDto);

    void removeOrder(long orderId);

    void createPaymentIssue(Long orderId, Long tid);

    void deletePaymentIssue(Long issueId);

    List<OrderPaymentIssueDto> getOrderPaymentIssues();
}
