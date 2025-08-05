package goodspace.backend.admin.service.question;

import goodspace.backend.admin.dto.question.*;
import goodspace.backend.fixture.AnswerFixture;
import goodspace.backend.fixture.GoodSpaceUserFixture;
import goodspace.backend.fixture.ImageFixture;
import goodspace.backend.fixture.QuestionFixture;
import goodspace.backend.qna.domain.Answer;
import goodspace.backend.qna.domain.Question;
import goodspace.backend.qna.domain.QuestionFile;
import goodspace.backend.qna.domain.QuestionStatus;
import goodspace.backend.qna.repository.AnswerRepository;
import goodspace.backend.qna.repository.QuestionFileRepository;
import goodspace.backend.qna.repository.QuestionRepository;
import goodspace.backend.user.domain.User;
import goodspace.backend.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class AdminQuestionServiceTest {
    static final Supplier<EntityNotFoundException> DTO_NOT_FOUND = () -> new EntityNotFoundException("DTO를 찾을 수 없습니다.");

    static final String DEFAULT_CONTENT = "Default Content";
    static final String CONTENT_FOR_UPDATE = "Updated Content";

    @Autowired
    AdminQuestionService adminQuestionService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    AnswerRepository answerRepository;
    @Autowired
    QuestionFileRepository questionFileRepository;

    User user;
    Question question;
    Question notAnsweredQuestion;
    Question answeredQuestion;
    Question emptyFileQuestion;
    List<Question> existQuestions;
    Answer answer;
    QuestionFile questionFile1;
    QuestionFile questionFile2;
    List<QuestionFile> questionFiles;

    @BeforeEach
    void resetEntities() throws IOException {
        question = questionRepository.save(QuestionFixture.DELIVERY.getInstance());
        notAnsweredQuestion = questionRepository.save(QuestionFixture.ORDER.getInstance());
        answeredQuestion = questionRepository.save(QuestionFixture.ITEM.getInstance());
        emptyFileQuestion = questionRepository.save(QuestionFixture.B.getInstance());
        existQuestions = List.of(question, notAnsweredQuestion, answeredQuestion, emptyFileQuestion);

        user = userRepository.save(GoodSpaceUserFixture.DEFAULT.getInstance());
        user.addQuestion(question);
        user.addQuestion(notAnsweredQuestion);
        user.addQuestion(answeredQuestion);
        user.addQuestion(emptyFileQuestion);

        answer = answerRepository.save(AnswerFixture.A.getInstance());
        answeredQuestion.setAnswer(answer);

        questionFile1 = questionFileRepository.save(getQuestionFileFromImageFixture(ImageFixture.JAVA));
        questionFile2 = questionFileRepository.save(getQuestionFileFromImageFixture(ImageFixture.KOTLIN));
        questionFiles = List.of(questionFile1, questionFile2);
    }

    @Nested
    class registerAnswer {
        @Test
        @DisplayName("문의에 답변을 등록한다")
        void registerAnswerToQuestion() {
            // given
            AnswerRegisterRequestDto requestDto = AnswerRegisterRequestDto.builder()
                    .questionId(notAnsweredQuestion.getId())
                    .content(DEFAULT_CONTENT)
                    .build();

            // when
            adminQuestionService.registerAnswer(requestDto);

            // then
            Answer registeredAnswer = notAnsweredQuestion.getAnswer();
            assertThat(registeredAnswer.getContent()).isEqualTo(DEFAULT_CONTENT);
        }

        @Test
        @DisplayName("문의의 상태를 COMPLETED로 변경한다")
        void changeStatusOfQuestionToCompleted() {
            // given
            AnswerRegisterRequestDto requestDto = AnswerRegisterRequestDto.builder()
                    .questionId(notAnsweredQuestion.getId())
                    .content(DEFAULT_CONTENT)
                    .build();

            // when
            adminQuestionService.registerAnswer(requestDto);

            // then
            QuestionStatus status = notAnsweredQuestion.getQuestionStatus();
            assertThat(status).isSameAs(QuestionStatus.COMPLETED);
        }
    }

    @Nested
    class updateAnswer {
        @Test
        @DisplayName("문의의 내용을 수정한다")
        void updateContentOfAnswer() {
            // given
            AnswerUpdateRequestDto requestDto = AnswerUpdateRequestDto.builder()
                    .answerId(answer.getId())
                    .content(CONTENT_FOR_UPDATE)
                    .build();

            // when
            adminQuestionService.updateAnswer(requestDto);

            // then
            assertThat(answer.getContent()).isEqualTo(CONTENT_FOR_UPDATE);
        }
    }

    @Nested
    class findQuestions {
        @Test
        @DisplayName("모든 문의를 조회한다")
        void findQuestionsOfUser() {
            List<QuestionInfoResponseDto> responseDtos = adminQuestionService.findQuestions();

            assertThat(responseDtos.size()).isEqualTo(existQuestions.size());

            for (Question existQuestion : existQuestions) {
                QuestionInfoResponseDto responseDto = findDtoById(existQuestion.getId(), responseDtos);

                assertThat(isEqual(existQuestion, responseDto)).isTrue();
            }
        }
    }

    @Nested
    class findQuestionById {
        @Test
        @DisplayName("ID와 일치하는 문의를 조회한다")
        void findSingleQuestionById() {
            QuestionAndAnswerResponseDto responseDto = adminQuestionService.findQuestionById(answeredQuestion.getId());

            assertThat(isEqual(answeredQuestion, responseDto.question())).isTrue();
            assertThat(isEqual(answer, responseDto.answer())).isTrue();
        }

        @Test
        @DisplayName("답변이 없는 문의라면 답변 속성을 null로 반환한다")
        void answerIsNullIfAnswerIsNotExist() {
            QuestionAndAnswerResponseDto responseDto = adminQuestionService.findQuestionById(notAnsweredQuestion.getId());

            assertThat(responseDto.answer()).isNull();
        }
    }

    @Nested
    class getFiles {
        @Test
        @DisplayName("문의에 첨부된 파일들을 ZIP으로 압축해 반환한다")
        void returnFilesByZip() throws Exception {
            // given
            question.addQuestionFiles(questionFiles);

            // when
            byte[] zippedFiles = adminQuestionService.getFiles(question.getId());

            // then
            try (ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(zippedFiles);
                 ZipInputStream zipInputStream = new ZipInputStream(arrayInputStream)) {

                int count = 0;
                ZipEntry entry;
                while ((entry = zipInputStream.getNextEntry()) != null) {
                    assertThat(isEntryMatchWithFile(entry, zipInputStream, questionFiles)).isTrue();
                    count++;
                    zipInputStream.closeEntry();
                }

                assertThat(count).isEqualTo(questionFiles.size());
            }
        }

        @Test
        @DisplayName("첨부된 파일이 없다면 빈 배열을 반환한다")
        void ifHasNotFileThenReturnEmptyArray() {
            byte[] files = adminQuestionService.getFiles(emptyFileQuestion.getId());

            assertThat(files).isEmpty();
        }
    }

    private QuestionFile getQuestionFileFromImageFixture(ImageFixture imageFixture) throws IOException {
        MultipartFile image = imageFixture.getImage();

        return QuestionFile.builder()
                .data(image.getBytes())
                .mimeType(image.getContentType())
                .name(getNotNullFileName(image, imageFixture.name()))
                .build();
    }

    private String getNotNullFileName(MultipartFile file, String defaultName) {
        return file.getOriginalFilename() != null ? file.getOriginalFilename() : defaultName + ".bin";
    }

    private QuestionInfoResponseDto findDtoById(long id, List<QuestionInfoResponseDto> dtos) {
        return dtos.stream()
                .filter(dto -> dto.id().equals(id))
                .findAny()
                .orElseThrow(DTO_NOT_FOUND);
    }

    private boolean isEqual(Question question, QuestionInfoResponseDto dto) {
        return question.getId().equals(dto.id()) &&
                question.getTitle().equals(dto.title()) &&
                question.getContent().equals(dto.content()) &&
                question.getQuestionType().equals(dto.questionType()) &&
                question.getQuestionStatus().equals(dto.questionStatus()) &&
                question.getUserEmail().equals(dto.userEmail()) &&
                question.getCreatedAt().equals(dto.createdAt());
    }

    private boolean isEqual(Answer answer, AnswerInfoResponseDto dto) {
        return answer.getId().equals(dto.id()) &&
                answer.getContent().equals(dto.content()) &&
                answer.getUpdatedAt().equals(dto.updatedAt());
    }

    private boolean isEntryMatchWithFile(ZipEntry entry, ZipInputStream zis, List<QuestionFile> files) throws IOException {
        Optional<QuestionFile> matchedFileOptional = files.stream()
                .filter(f -> f.getName().equals(entry.getName()))
                .findFirst();

        if (matchedFileOptional.isEmpty()) {
            return false;
        }

        QuestionFile matchedFile = matchedFileOptional.get();
        byte[] buffer = zis.readAllBytes();

        return Arrays.equals(buffer, matchedFile.getData());
    }
}
