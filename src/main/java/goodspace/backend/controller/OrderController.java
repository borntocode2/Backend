package goodspace.backend.controller;

import goodspace.backend.domain.Order;
import goodspace.backend.dto.OrderRequestDto;
import goodspace.backend.dto.OrderResponseDto;
import goodspace.backend.payment.service.NicePayService;
import goodspace.backend.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody OrderRequestDto orderRequest) {
        orderService.saveOrder(orderRequest);
        return ResponseEntity.ok("Order created");
    }
}
