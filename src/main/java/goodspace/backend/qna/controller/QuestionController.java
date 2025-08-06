package goodspace.backend.qna.controller;

import goodspace.backend.global.swagger.CreateQuestionSwaggerSchema;
import goodspace.backend.qna.dto.AllQuestionResponseDto;
import goodspace.backend.qna.dto.QuestionRequestDto;
import goodspace.backend.qna.dto.QuestionResponseDto;
import goodspace.backend.qna.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/qna")
@Tag(
        name = "QnA 질문 API",
        description = "QnA 질문 CRUD"
)
public class QuestionController {
    private final QuestionService questionService;

    @Operation(
            summary = "질문 작성",
            description = "파일과 함께 질문을 작성합니다. 질문 key : question, 파일 key : file \n question과 file을 나누어서 받을 때, Swagger 상, requestBody가 명확히 정의되지 않기 때문에 서버 개발자에게 문의바랍니다." ,
            requestBody = @RequestBody(
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(implementation = CreateQuestionSwaggerSchema.class)
                    )
            )
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createQuestion(Principal principal,
                                                 @RequestPart("question") QuestionRequestDto questionDto,
                                                 @RequestPart(value = "file", required = false) List<MultipartFile> files
    ) throws IOException {
        return ResponseEntity.ok(questionService.createQuestion(principal, questionDto, files));
    }

    @Operation(
            summary = "질문 단건 조회",
            description = "질문 ID를 통해 질문을 조회합니다."
    )
    @GetMapping("/question/getQuestion/{id}")
    public ResponseEntity<QuestionResponseDto> getQuestion(@PathVariable Long id) {
        return ResponseEntity.ok(questionService.getQuestion(id));
    }

    @Operation(
            summary = "질문 삭제",
            description = "질문ID를 통해 질문을 삭제합니다."
    )
    @DeleteMapping("/question/delete/{id}")
    public ResponseEntity<String> deleteQuestion(@PathVariable Long id) {
        return ResponseEntity.ok(questionService.deleteQuestion(id));
    }

    @Operation(
            summary = "유저의 질문 조회",
            description = "JWT토큰을 통해 모든 유저의 질문을 조회합니다."
    )
    @GetMapping("/question/getUserQuestions")
    public ResponseEntity<List<AllQuestionResponseDto>> getUserQuestions(Principal principal) {
        return ResponseEntity.ok(questionService.getAllQuestions(principal));
    }

    @Operation(
            summary = "질문 수정",
            description = "질문ID를 통해 질문을 수정합니다."
    )
    @PatchMapping("/question/modifyQuestions/{id}")
    public ResponseEntity<String> modifyQuestion(@PathVariable Long id,
                                                 @RequestPart("question") QuestionRequestDto questionDto,
                                                 @RequestPart(value = "file", required = false) List<MultipartFile> files
    ) throws IOException {
        return ResponseEntity.ok(questionService.modifyQuestion(id, questionDto, files));
    }

}
