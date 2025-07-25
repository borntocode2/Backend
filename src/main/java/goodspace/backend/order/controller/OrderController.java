package goodspace.backend.order.controller;

import goodspace.backend.order.dto.OrderRequestDto;
import goodspace.backend.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orderTest")
public class OrderController {
    private final OrderService orderService;

    //TODO - order에 delivery 임베더블 해놨으니, 로직 수정하시오.
    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody OrderRequestDto orderRequest) {
        orderService.saveOrder(orderRequest);
        return ResponseEntity.ok("Order created");
    }
}

