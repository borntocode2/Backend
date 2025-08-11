package goodspace.backend.admin.controller;

import goodspace.backend.admin.dto.order.OrderInfoResponseDto;
import goodspace.backend.admin.dto.order.OrderUpdateRequestDto;
import goodspace.backend.admin.service.order.OrderManageService;
import goodspace.backend.order.dto.OrderPaymentIssueDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/order")
@RequiredArgsConstructor
@Tag(
        name = "주문 관리 API(관리자 전용)",
        description = "주문 조회/수정/삭제 관련 기능"
)
public class OrderManageController {
    private final OrderManageService orderManageService;

    @GetMapping
    @Operation(
            summary = "주문 조회",
            description = "모든 주문 정보를 조회합니다."
    )
    public ResponseEntity<List<OrderInfoResponseDto>> getOrders() {
        List<OrderInfoResponseDto> response = orderManageService.getOrders();

        return ResponseEntity.ok(response);
    }

    @PutMapping
    @Operation(
            summary = "주문 정보 수정",
            description = "주문의 배송지 정보와 승인 정보를 수정합니다."
    )
    public ResponseEntity<Void> updateOrderInfo(@RequestBody OrderUpdateRequestDto requestDto) {
        orderManageService.updateOrder(requestDto);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    @Operation(
            summary = "주문 삭제",
            description = "주문을 삭제합니다."
    )
    public ResponseEntity<Void> deleteOrder(@RequestParam Long orderId) {
        orderManageService.removeOrder(orderId);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/payment/issue")
    @Operation(
            summary = "결제 에러 이슈",
            description = "개발자가 해결해야할 결제 이슈입니다."
    )
    public ResponseEntity<Void> paymentIssue(@RequestParam Long orderId, @RequestParam Long tid) {
        orderManageService.createPaymentIssue(orderId, tid);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/payment/Issue")
    @Operation(
            summary = "결제 에러 이슈 삭제",
            description = "개발자가 결제 에러 이슈를 해결하여 삭제합니다."
    )
    public ResponseEntity<Void> deletePaymentIssue(@RequestParam Long issueId) {
        orderManageService.deletePaymentIssue(issueId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/payment/issue")
    @Operation(
            summary = "결제 에러 이슈 확인",
            description = "결제 에러 이슈 리스트를 확인합니다."
    )
    public ResponseEntity<List<OrderPaymentIssueDto>> getPaymentIssues() {
        return ResponseEntity.ok(orderManageService.getOrderPaymentIssues());


    }
}
