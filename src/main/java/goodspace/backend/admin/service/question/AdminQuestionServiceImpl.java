package goodspace.backend.admin.service.question;

import goodspace.backend.admin.dto.question.AnswerUpdateRequestDto;
import goodspace.backend.admin.dto.question.QuestionAndAnswerResponseDto;
import goodspace.backend.admin.dto.question.QuestionInfoResponseDto;
import goodspace.backend.qna.domain.Answer;
import goodspace.backend.qna.domain.Question;
import goodspace.backend.qna.domain.QuestionFile;
import goodspace.backend.qna.domain.QuestionStatus;
import goodspace.backend.admin.dto.question.AnswerRegisterRequestDto;
import goodspace.backend.qna.repository.AnswerRepository;
import goodspace.backend.qna.repository.QuestionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@RequiredArgsConstructor
public class AdminQuestionServiceImpl implements AdminQuestionService {
    private static final Supplier<EntityNotFoundException> QUESTION_NOT_FOUND = () -> new EntityNotFoundException("문의를 찾을 수 없습니다.");
    private static final Supplier<EntityNotFoundException> ANSWER_NOT_FOUND = () -> new EntityNotFoundException("답변을 찾을 수 없습니다.");
    private static final byte[] EMPTY_ZIP = new byte[0];

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    @Override
    @Transactional
    public String registerAnswer(AnswerRegisterRequestDto requestDto) {
        Question question = questionRepository.findById(requestDto.questionId())
                .orElseThrow(QUESTION_NOT_FOUND);

        Answer answer = Answer.builder()
                .content(requestDto.content())
                .question(question)
                .build();

        question.setAnswer(answer);
        question.setQuestionStatus(QuestionStatus.COMPLETED);

        return "답변 등록이 완료되었습니다.";
    }

    @Override
    @Transactional
    public void updateAnswer(AnswerUpdateRequestDto requestDto) {
        Answer answer = answerRepository.findById(requestDto.answerId())
                .orElseThrow(ANSWER_NOT_FOUND);

        answer.setContent(requestDto.content());
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionInfoResponseDto> findQuestions() {
        return questionRepository.findAll().stream()
                .map(QuestionInfoResponseDto::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public QuestionAndAnswerResponseDto findQuestionById(long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(QUESTION_NOT_FOUND);

        return QuestionAndAnswerResponseDto.from(question);
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] getFiles(long questionId) {
        List<QuestionFile> files = questionRepository.findById(questionId)
                .orElseThrow(QUESTION_NOT_FOUND)
                .getQuestionFiles();

        if (files.isEmpty()) {
            return EMPTY_ZIP;
        }

        return zip(files);
    }

    private byte[] zip(List<QuestionFile> files) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {

            for (QuestionFile file : files) {
                ZipEntry entry = new ZipEntry(file.getName());
                zipOutputStream.putNextEntry(entry);
                zipOutputStream.write(file.getData());
                zipOutputStream.closeEntry();
            }

            zipOutputStream.finish();

            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("파일 압축 중 오류가 발생했습니다.", e);
        }
    }
}
