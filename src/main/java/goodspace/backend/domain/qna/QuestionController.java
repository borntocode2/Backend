package goodspace.backend.domain.qna;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/qna")
public class QuestionController {
    private final QuestionService questionService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createQuestion(
            @RequestPart("question") QuestionRequestDto questionDto,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) throws IOException {
        return ResponseEntity.ok(questionService.createQuestion(questionDto, file));
    }

    @GetMapping("/file/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable Long id) {
        return questionService.downloadFile(id);
    }
}
