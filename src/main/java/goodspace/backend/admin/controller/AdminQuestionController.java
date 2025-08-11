package goodspace.backend.admin.controller;

import goodspace.backend.admin.dto.question.AnswerUpdateRequestDto;
import goodspace.backend.admin.dto.question.QuestionAndAnswerResponseDto;
import goodspace.backend.admin.dto.question.QuestionInfoResponseDto;
import goodspace.backend.admin.service.question.AdminQuestionService;
import goodspace.backend.admin.dto.question.AnswerRegisterRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/question")
@RequiredArgsConstructor
@Tag(
        name = "QNA게시판(관리자 전용)",
        description = "답변 등록 및 질문 상태 변경"
)
public class AdminQuestionController {
    private final AdminQuestionService adminQuestionService;

    @GetMapping
    @Operation(
            summary = "전체 문의 조회",
            description = "모든 문의를 조회합니다."
    )
    public ResponseEntity<List<QuestionInfoResponseDto>> getQuestions() {
        List<QuestionInfoResponseDto> response = adminQuestionService.findQuestions();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{questionId}")
    @Operation(
            summary = "문의 단건 조회",
            description = "ID를 통해 문의를 조회합니다."
    )
    public ResponseEntity<QuestionAndAnswerResponseDto> getQuestionById(@PathVariable Long questionId) {
        QuestionAndAnswerResponseDto response = adminQuestionService.findQuestionById(questionId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{questionId}/file")
    @Operation(
            summary = "문의 파일 조회",
            description = "해당 문의에 첨부된 파일을 ZIP으로 압축해 반환합니다."
    )
    public ResponseEntity<byte[]> getFiles(@PathVariable Long questionId) {
        byte[] zipBytes = adminQuestionService.getFiles(questionId);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=question_" + questionId + "_files.zip")
                .header("Content-Type", "application/zip")
                .body(zipBytes);
    }

    @PostMapping("/answer")
    @Operation(
            summary = "답변 등록",
            description = "문의에 대한 답변을 등록하고, 문의의 상태를 COMPLETED로 변경합니다."
    )
    public ResponseEntity<String> registerAnswer(@RequestBody AnswerRegisterRequestDto answerDto) {
        return ResponseEntity.ok(adminQuestionService.registerAnswer(answerDto));
    }

    @PutMapping("/answer")
    @Operation(
            summary = "답변 수정",
            description = "문의에 대한 답변을 수정합니다."
    )
    public ResponseEntity<Void> updateAnswer(@RequestBody AnswerUpdateRequestDto requestDto) {
        adminQuestionService.updateAnswer(requestDto);

        return ResponseEntity.ok().build();
    }
}
