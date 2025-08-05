package goodspace.backend.admin.service.question;

import goodspace.backend.admin.dto.question.AnswerUpdateRequestDto;
import goodspace.backend.admin.dto.question.QuestionAndAnswerResponseDto;
import goodspace.backend.admin.dto.question.QuestionInfoResponseDto;
import goodspace.backend.admin.dto.question.AnswerRegisterRequestDto;

import java.util.List;

public interface AdminQuestionService {
    String registerAnswer(AnswerRegisterRequestDto requestDto);

    void updateAnswer(AnswerUpdateRequestDto requestDto);

    List<QuestionInfoResponseDto> findQuestions();

    QuestionAndAnswerResponseDto findQuestionById(long questionId);

    byte[] getFiles(long questionId);
}
