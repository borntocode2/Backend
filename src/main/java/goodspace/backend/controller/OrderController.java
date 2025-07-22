package goodspace.backend.controller;

import goodspace.backend.domain.Order;
import goodspace.backend.dto.OrderRequestDto;
import goodspace.backend.dto.OrderResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderRequestDto orderRequest) {

    }
}
