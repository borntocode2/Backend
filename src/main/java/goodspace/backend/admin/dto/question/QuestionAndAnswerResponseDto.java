package goodspace.backend.admin.dto.question;

import goodspace.backend.qna.domain.Answer;
import goodspace.backend.qna.domain.Question;
import lombok.Builder;

@Builder
public record QuestionAndAnswerResponseDto(
        QuestionInfoResponseDto question,
        AnswerInfoResponseDto answer
) {
    public static QuestionAndAnswerResponseDto from(Question question) {
        QuestionInfoResponseDto questionDto = QuestionInfoResponseDto.from(question);
        AnswerInfoResponseDto answerDto = getDtoIfNotNull(question.getAnswer());

        return QuestionAndAnswerResponseDto.builder()
                .question(questionDto)
                .answer(answerDto)
                .build();
    }

    private static AnswerInfoResponseDto getDtoIfNotNull(Answer answer) {
        if (answer == null) {
            return null;
        }

        return AnswerInfoResponseDto.from(answer);
    }
}
