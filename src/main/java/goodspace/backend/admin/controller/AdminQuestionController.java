package goodspace.backend.admin.controller;

import goodspace.backend.admin.service.AdminQuestionService;
import goodspace.backend.qna.dto.AnswerDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/question")
@RequiredArgsConstructor
@Tag(
        name = "관리자 전용 QNA게시판",
        description = "답변 등록 및 질문 상태 변경"
)
public class AdminQuestionController {
    private final AdminQuestionService adminQuestionService;

    @PatchMapping("/question/changeStatus/{questionId}")
    public ResponseEntity<String> changeStatusToCompleted(@PathVariable Long questionId, @RequestBody AnswerDto answerDto) {
        return ResponseEntity.ok(adminQuestionService.registerAnswer(questionId, answerDto));
    }
}
