package goodspace.backend.qna.controller;

import goodspace.backend.global.swagger.CreateQuestionSwaggerSchema;
import goodspace.backend.qna.dto.AllQuestionResponseDto;
import goodspace.backend.qna.dto.QuestionRequestDto;
import goodspace.backend.qna.dto.QuestionResponseDto;
import goodspace.backend.qna.repository.QuestionRepository;
import goodspace.backend.qna.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
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
public class QuestionController {
    private final QuestionService questionService;
    private final QuestionRepository questionRepository;

    @Operation(
            summary = "질문 작성",
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

    @GetMapping("/question/getQuestion/{id}")
    public ResponseEntity<QuestionResponseDto> getQuestion(@PathVariable Long id) {
        return ResponseEntity.ok(questionService.getQuestion(id));
    }

    @DeleteMapping("/question/delete/{id}")
    public ResponseEntity<String> deleteQuestion(@PathVariable Long id) {
        return ResponseEntity.ok(questionService.deleteQuestion(id));
    }

    @GetMapping("/question/getUserQuestions")
    public ResponseEntity<List<AllQuestionResponseDto>> getUserQuestions(Principal principal) {
        return ResponseEntity.ok(questionService.getAllQuestions(principal));
    }

    @PatchMapping("/quesiton/modifyQuestions/{id}")
    public ResponseEntity<String> modifyQuestion(@PathVariable Long id, @RequestBody QuestionRequestDto questionDto) {
        return ResponseEntity.ok(questionService.modifyQuestion(id, questionDto));
    }

}
