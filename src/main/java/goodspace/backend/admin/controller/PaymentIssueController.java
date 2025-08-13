package goodspace.backend.admin.controller;

import goodspace.backend.admin.dto.order.PaymentIssueResponseDto;
import goodspace.backend.admin.service.issue.PaymentIssueService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment-issue")
@RequiredArgsConstructor
public class PaymentIssueController {
    private final PaymentIssueService paymentIssueService;

    @GetMapping
    @Operation(
            summary = "결제 에러 이슈 조회",
            description = "결제중 발생한 문제를 모두 조회합니다."
    )
    public ResponseEntity<List<PaymentIssueResponseDto>> findPaymentIssues() {
        List<PaymentIssueResponseDto> response = paymentIssueService.find();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(
            summary = "결제 에러 이슈 생성",
            description = "개발자가 해결해야 할 결제 이슈를 생성합니다."
    )
    public ResponseEntity<Void> createPaymentIssue(@RequestParam Long orderId, @RequestParam Long tid) {
        paymentIssueService.create(orderId, tid);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    @Operation(
            summary = "결제 에러 이슈 해결(삭제)",
            description = "결제 에러 이슈를 해결했다 판단하고 삭제합니다."
    )
    public ResponseEntity<Void> deletePaymentIssue(@RequestParam Long issueId) {
        paymentIssueService.remove(issueId);

        return ResponseEntity.noContent().build();
    }
}
