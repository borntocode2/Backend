package goodspace.backend.qna.service;

import goodspace.backend.qna.repository.QuestionFileRepository;
import goodspace.backend.qna.repository.QuestionRepository;
import goodspace.backend.qna.domain.Question;
import goodspace.backend.qna.domain.QuestionFile;
import goodspace.backend.qna.domain.QuestionStatus;
import goodspace.backend.qna.dto.QuestionRequestDto;
import goodspace.backend.qna.dto.QuestionResponseDto;
import goodspace.backend.user.repository.UserRepository;
import goodspace.backend.global.security.TokenProvider;
import goodspace.backend.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final QuestionFileRepository fileRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    public String createQuestion(Principal principal, QuestionRequestDto dto, List<MultipartFile> files) throws IOException {
        Question question = Question.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .questionType(dto.getType())
                .questionStatus(QuestionStatus.WAITING)
                .user(userRepository.findById(TokenProvider.getUserIdFromPrincipal(principal))
                        .orElseThrow(() -> new RuntimeException("질문을 생성하는 도중, 인증 객체의 유저 정보를 서버에서 찾을 수 없습니다.")))
                .build();

        if (files != null && !files.isEmpty()) {
            List<QuestionFile> fileEntities = files.stream()
                    .map(file -> {
                        try {
                            return QuestionFile.builder()
                                    .name(file.getOriginalFilename())
                                    .data(file.getBytes())
                                    .mimeType(file.getContentType())
                                    .question(question)
                                    .build();
                        } catch (IOException e) {
                            throw new RuntimeException("파일 변환 실패", e);
                        }
                    })
                            .collect(Collectors.toList());

            question.setQuestionFiles(fileEntities);
        }
        questionRepository.save(question);

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

    @Transactional
    public QuestionResponseDto getQuestion(Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 id의 question은 questionRespository에서 찾을 수 없습니다. "));

        List<Long> fileId = question.getQuestionFiles().stream()
                .map(QuestionFile::getId)  // 각 파일의 ID 추출
                .collect(Collectors.toList());

        System.out.println("aaaaaaaaaaaaaaaaaaaaa : " + fileId);
        return QuestionResponseDto.builder()
                .title(question.getTitle())
                .content(question.getContent())
                .userId(question.getUser().getId())
                .type(question.getQuestionType())
                .status(question.getQuestionStatus())
                .answer(question.getAnswer())
                .fileIds(fileId)
                .build();
    }
    // TODO : setQuestionType
}

