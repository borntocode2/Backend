package goodspace.backend.qna;

import goodspace.backend.fixture.QuestionFixture;
import goodspace.backend.qna.domain.Question;
import goodspace.backend.qna.domain.QuestionFile;
import goodspace.backend.qna.domain.QuestionStatus;
import goodspace.backend.qna.domain.QuestionType;
import goodspace.backend.qna.dto.AllQuestionResponseDto;
import goodspace.backend.qna.dto.QuestionRequestDto;
import goodspace.backend.qna.dto.QuestionResponseDto;
import goodspace.backend.qna.dto.QuestionFileDto;
import goodspace.backend.qna.repository.QuestionFileRepository;
import goodspace.backend.qna.repository.QuestionRepository;
import goodspace.backend.qna.service.QuestionService;
import goodspace.backend.user.domain.GoodSpaceUser;
import goodspace.backend.user.repository.UserRepository;
import goodspace.backend.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@DisplayName("QuestionService 통합 테스트")
class QnaServiceTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionFileRepository questionFileRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private QuestionService questionService;

    private GoodSpaceUser testUser;
    private Principal principal;

    @BeforeEach
    void setUp() {
        testUser = GoodSpaceUser.builder()
                .email("test@example.com")
                .password("password")
                .build();
        testUser = userRepository.save(testUser);

        principal = new Principal() {
            @Override
            public String getName() {
                return testUser.getId().toString();
            }
        };
    }

    @Test
    @DisplayName("질문 생성 성공 테스트")
    void createQuestion_Success() throws IOException {
        // given
        QuestionRequestDto testQuestionRequestDto = QuestionRequestDto.builder()
                .title("통합 테스트 질문 제목")
                .content("통합 테스트 질문 내용")
                .type(QuestionType.DELIVERY)
                .build();

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "테스트 파일 내용".getBytes()
        );
        List<MockMultipartFile> mockFiles = Arrays.asList(file);
        List<MultipartFile> files = mockFiles.stream()
                .map(f -> (MultipartFile) f)
                .collect(Collectors.toList());

        // when
        String result = questionService.createQuestion(principal, testQuestionRequestDto, files);

        // then
        assertThat(result).isEqualTo("Question 저장에 성공하였습니다.");

        List<Question> savedQuestions = questionRepository.findByUserId(testUser.getId());
        assertThat(savedQuestions).hasSize(1);
        assertThat(savedQuestions.get(0).getTitle()).isEqualTo("통합 테스트 질문 제목");
        assertThat(savedQuestions.get(0).getContent()).isEqualTo("통합 테스트 질문 내용");
        assertThat(savedQuestions.get(0).getQuestionType()).isEqualTo(QuestionType.DELIVERY);
        assertThat(savedQuestions.get(0).getUser().getId()).isEqualTo(testUser.getId());
    }

    @Test
    @DisplayName("질문 조회 테스트 - 파일을 포함한 경우")
    void getQuestion_WithFiles() throws IOException {
        // given
        Question testQuestion = QuestionFixture.DELIVERY.getInstance();
        testQuestion.setUser(testUser);
        final Question savedQuestion = questionRepository.save(testQuestion);

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "테스트 파일 내용".getBytes()
        );

        List<MockMultipartFile> mockFiles = Arrays.asList(file);

        List<QuestionFile> files = mockFiles.stream()
                .map(f -> {
                    try {
                        return QuestionFile.builder()
                                .name(f.getOriginalFilename())
                                .mimeType(f.getContentType())
                                .data(f.getBytes())
                                .question(savedQuestion)
                                .build();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

        savedQuestion.addQuestionFiles(files);
        final Question questionWithFiles = questionRepository.save(savedQuestion);

        QuestionResponseDto result = questionService.getQuestion(questionWithFiles.getId());

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo(questionWithFiles.getTitle());
        assertThat(result.getContent()).isEqualTo(questionWithFiles.getContent());
        assertThat(result.getQuestionFileDtos()).isNotNull();
        assertThat(result.getQuestionFileDtos()).hasSize(1);

        QuestionFileDto fileDto = result.getQuestionFileDtos().get(0);
        assertThat(fileDto.getData()).isEqualTo(files.get(0).getData());
        assertThat(fileDto.getMimeType()).isEqualTo(files.get(0).getMimeType());
        assertThat(fileDto.getName()).isEqualTo(files.get(0).getName());
    }

    @Test
    @DisplayName("질문 조회 성공 테스트")
    void getQuestion_Success() {
        Question testQuestion = QuestionFixture.DELIVERY.getInstance();
        testQuestion.setUser(testUser);
        testQuestion = questionRepository.save(testQuestion);

        QuestionResponseDto result = questionService.getQuestion(testQuestion.getId());

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo(testQuestion.getTitle());
        assertThat(result.getContent()).isEqualTo(testQuestion.getContent());
        assertThat(result.getType()).isEqualTo(testQuestion.getQuestionType());
        assertThat(result.getStatus()).isEqualTo(testQuestion.getQuestionStatus());
    }

    @Test
    @DisplayName("질문 조회 실패 테스트 - 질문을 찾을 수 없는 경우")
    void getQuestion_Failure_QuestionNotFound() {
        Long nonExistentQuestionId = 999L;

        assertThatThrownBy(() ->
                questionService.getQuestion(nonExistentQuestionId)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 id의 question은 questionRespository에서 찾을 수 없습니다");
    }

    @Test
    @DisplayName("사용자의 모든 질문 조회 성공 테스트")
    void getAllQuestions_Success() {
        Question question1 = QuestionFixture.DELIVERY.getInstance();
        question1.setUser(testUser);
        question1 = questionRepository.save(question1);

        Question question2 = QuestionFixture.ORDER.getInstance();
        question2.setUser(testUser);
        question2 = questionRepository.save(question2);

        List<AllQuestionResponseDto> result = questionService.getAllQuestions(principal);

        assertThat(result).hasSize(2);

        List<String> titles = result.stream()
                .map(AllQuestionResponseDto::getTitle)
                .collect(Collectors.toList());
        assertThat(titles).contains("DELIVERY title", "ORDER title");
    }

    @Test
    @DisplayName("질문 생성 실패 테스트 - 사용자를 찾을 수 없는 경우")
    void createQuestion_Failure_UserNotFound() {
        QuestionRequestDto testQuestionRequestDto = QuestionRequestDto.builder()
                .title("title")
                .content("content")
                .type(QuestionType.DELIVERY)
                .build();

        Principal nonExistentUserPrincipal = new Principal() {
            @Override
            public String getName() {
                return "999";
            }
        };

        assertThatThrownBy(() ->
                questionService.createQuestion(nonExistentUserPrincipal, testQuestionRequestDto, null)
        ).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("인증 객체의 유저 정보를 서버에서 찾을 수 없습니다");
    }

    @Test
    @DisplayName("질문 수정 성공 테스트")
    void modifyQuestion_Success() throws IOException {
        // given
        Question testQuestion = QuestionFixture.DELIVERY.getInstance();
        testQuestion.setUser(testUser);
        testQuestion = questionRepository.save(testQuestion);

        QuestionRequestDto testQuestionRequestDto = QuestionRequestDto.builder()
                .title("수정된 제목")
                .content("수정된 내용")
                .type(QuestionType.ORDER)
                .build();

        String result = questionService.modifyQuestion(testQuestion.getId(), testQuestionRequestDto, null);

        assertThat(result).isEqualTo("해당 질문의 수정이 성공하였습니다.");

        Question updatedQuestion = questionRepository.findById(testQuestion.getId()).orElse(null);
        assertThat(updatedQuestion).isNotNull();
        assertThat(updatedQuestion.getTitle()).isEqualTo("수정된 제목");
        assertThat(updatedQuestion.getContent()).isEqualTo("수정된 내용");
        assertThat(updatedQuestion.getQuestionType()).isEqualTo(QuestionType.ORDER);
    }

    @Test
    @DisplayName("질문 수정 실패 테스트 - 질문을 찾을 수 없는 경우")
    void modifyQuestion_Failure_QuestionNotFound() {
        QuestionRequestDto testQuestionRequestDto = QuestionRequestDto.builder()
                .title("title")
                .content("content")
                .type(QuestionType.DELIVERY)
                .build();

        Long nonExistentQuestionId = 999L;

        assertThatThrownBy(() ->
                questionService.modifyQuestion(nonExistentQuestionId, testQuestionRequestDto, null)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("수정하려는 질문을 데이터베이스에서 찾지 못하였습니다");
    }

    @Test
    @DisplayName("질문 삭제 성공 테스트")
    void deleteQuestion_Success() {
        Question testQuestion = QuestionFixture.DELIVERY.getInstance();
        testQuestion.setUser(testUser);
        testQuestion = questionRepository.save(testQuestion);

        Long questionId = testQuestion.getId();

        String result = questionService.deleteQuestion(questionId);

        assertThat(result).isEqualTo("질문이 성공적으로 삭제되었습니다.");

        assertThat(questionRepository.findById(questionId)).isEmpty();
    }

    @Test
    @DisplayName("질문 삭제 실패 테스트 - 질문을 찾을 수 없는 경우")
    void deleteQuestion_Failure_QuestionNotFound() {
        Long nonExistentQuestionId = 999L;

        assertThatThrownBy(() ->
                questionService.deleteQuestion(nonExistentQuestionId)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 ID의 질문을 찾을 수 없습니다");
    }
}
