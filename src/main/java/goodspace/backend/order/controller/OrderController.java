package goodspace.backend.order.controller;

import goodspace.backend.order.dto.OrderRequestDto;
import goodspace.backend.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    @Operation(
            summary = "주문 생성",
            description = "결제가 완료되기 전, order를 생성합니다."
    )
    @PostMapping
    public ResponseEntity<Long> createOrder(Principal principal, @RequestBody OrderRequestDto orderRequest) {
        return ResponseEntity.ok(orderService.saveOrder(principal, orderRequest));
    }

    @Operation(
            summary = "주문 삭제",
            description = "생성된 order를 삭제합니다."
    )
    @DeleteMapping("/cancel/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }
}

