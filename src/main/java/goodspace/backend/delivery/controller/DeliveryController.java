package goodspace.backend.delivery.controller;

import goodspace.backend.delivery.dto.DeliveryRequestDto;
import goodspace.backend.delivery.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DeliveryController {
    private final DeliveryService deliveryService;

    @PostMapping("/delivery/callTrace")
    public ResponseEntity<String> callTrace(@RequestBody DeliveryRequestDto request) {
        try {
            return ResponseEntity.ok(deliveryService.RegistrationMappingWithOrderIdAfterCallTrace(
                    request.getOrderId(),
                    request.getRegistrationNumber())
            );
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            // JSON/XML 변환 과정에서 발생하는 예외 처리
            return ResponseEntity
                    .status(500)
                    .body("데이터 변환 중 오류가 발생했습니다: " + e.getMessage());
        } catch (NullPointerException e) {
            // 응답 데이터가 null이어서 생긴 문제 처리
            return ResponseEntity
                    .badRequest()
                    .body("외부 API 응답이 null입니다.");
        } catch (Exception e) {
            // 그 외 모든 예외 처리
            return ResponseEntity
                    .status(500)
                    .body("서버 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}
