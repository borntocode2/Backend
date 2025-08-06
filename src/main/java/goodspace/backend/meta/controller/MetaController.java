package goodspace.backend.meta.controller;

import goodspace.backend.meta.dto.*;
import goodspace.backend.meta.service.MetaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/meta")
@RequiredArgsConstructor
@Tag(
        name = "Meta API",
        description = "상수로 관리하는 타입/카테고리 관련 기능"
)
public class MetaController {
    private final MetaService metaService;

    @GetMapping("/client-type")
    @Operation(
            summary = "클라이언트 종류",
            description = "모든 Client Type을 반환합니다."
    )
    public ResponseEntity<List<ClientTypeDto>> getClientTypes() {
        List<ClientTypeDto> response = metaService.getClientTypes();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/oauth-type")
    @Operation(
            summary = "OAuth 종류",
            description = "모든 OAuth Type을 반환합니다."
    )
    public ResponseEntity<List<OAuthTypeDto>> getOAuthTypes() {
        List<OAuthTypeDto> response = metaService.getOauthTypes();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/order-status")
    @Operation(
            summary = "주문 상태",
            description = "모든 Order Status를 반환합니다."
    )
    public ResponseEntity<List<OrderStatusDto>> getOrderStatuses() {
        List<OrderStatusDto> response = metaService.getOrderStatuses();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/question-status")
    @Operation(
            summary = "문의 상태",
            description = "모든 Question Status를 반환합니다."
    )
    public ResponseEntity<List<QuestionStatusDto>> getQuestionStatuses() {
        List<QuestionStatusDto> response = metaService.getQuestionStatuses();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/question-type")
    @Operation(
            summary = "문의 유형",
            description = "모든 Question Type을 반환합니다."
    )
    public ResponseEntity<List<QuestionTypeDto>> getQuestionTypes() {
        List<QuestionTypeDto> response = metaService.getQuestionTypes();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/register-status")
    @Operation(
            summary = "등록 상태",
            description = "모든 Register Status를 반환합니다."
    )
    public ResponseEntity<List<RegisterStatusDto>> getRegisterStatuses() {
        List<RegisterStatusDto> response = metaService.getRegisterStatuses();

        return ResponseEntity.ok(response);
    }
}
