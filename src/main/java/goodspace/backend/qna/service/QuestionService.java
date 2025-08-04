package goodspace.backend.qna.service;

import goodspace.backend.qna.dto.AllQuestionResponseDto;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final QuestionFileRepository fileRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    @Transactional
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

    public ResponseEntity<byte[]> downloadFilesAsZip(List<Long> ids) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zipOut = new ZipOutputStream(baos);

        for (Long id : ids) {
            Question question = questionRepository.findById(id).orElse(null);

            if (question != null) {
                for (QuestionFile file : question.getQuestionFiles()) {
                    ZipEntry zipEntry = new ZipEntry(file.getName());
                    zipOut.putNextEntry(zipEntry);
                    zipOut.write(file.getData());
                    zipOut.closeEntry();
                }
            }
        }

        zipOut.close();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"questions.zip\"")
                .body(baos.toByteArray());
    }

    @Transactional
    public QuestionResponseDto getQuestion(Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 id의 question은 questionRespository에서 찾을 수 없습니다. "));

        List<Long> fileId = question.getQuestionFiles().stream()
                .map(QuestionFile::getId)  // 각 파일의 ID 추출
                .collect(Collectors.toList());

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

    @Transactional
    public List<AllQuestionResponseDto> getAllQuestions(Long id) {
        List<Question> questions = questionRepository.findByUserId(id);

        return questions.stream()
                .map(q -> AllQuestionResponseDto.builder()
                        .questionId(q.getId())
                        .title(q.getTitle())
                        .content(q.getContent())
                        .type(q.getQuestionType())
                        .status(q.getQuestionStatus())
                        .createdAt(q.getCreatedAt())
                        .updatedAt(q.getUpdatedAt())
                        .build()
                )
                .collect(Collectors.toList());
    }

    @Transactional
    public String deleteQuestion(Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 질문을 찾을 수 없습니다."));

        // (선택) 연관된 파일이나 답변이 있는지 검증/로그
        if (!question.getQuestionFiles().isEmpty()) {
            System.out.println("경고: 삭제하려는 질문에 첨부파일이 존재합니다.");
        }

        if (question.getAnswer() != null) {
            System.out.println("경고: 삭제하려는 질문에 답변이 존재합니다.");
        }

        questionRepository.delete(question);
        return "질문이 성공적으로 삭제되었습니다.";
    }
}

