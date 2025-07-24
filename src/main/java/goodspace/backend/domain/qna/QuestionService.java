package goodspace.backend.domain.qna;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final QuestionFileRepository fileRepository;

    public String createQuestion(QuestionRequestDto dto, MultipartFile file) throws IOException {
        Question question = Question.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .questionType(dto.getType())
                .questionStatus(QuestionStatus.WAITING)
                .build();
        Question saved = questionRepository.save(question);

        if (file != null && !file.isEmpty()) {
            QuestionFile questionFile = QuestionFile.builder()
                    .data(file.getBytes())
                    .extension(file.getOriginalFilename())
                    .mimeType(file.getContentType())
                    .name(file.getOriginalFilename())
                    .question(question)
                    .build();

            fileRepository.save(questionFile);
        }
        return "Question 저장에 성공하였습니다.";
    }

    public ResponseEntity<byte[]> downloadFile(Long id) {
        QuestionFile file = fileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("파일 없음"));

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getMimeType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                .body(file.getData());
    }
    // TODO : setQuestionType
}

