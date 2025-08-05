package goodspace.backend.qna.dto;

import goodspace.backend.qna.domain.Answer;
import goodspace.backend.qna.domain.QuestionStatus;
import goodspace.backend.qna.domain.QuestionType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class QuestionResponseDto {
    private String title;
    private String content;
    private QuestionType type;
    private QuestionStatus status;
    private LocalDateTime createdAt;
    private AnswerDto answerDto;
    private List<QuestionFileDto> questionFileDtos;
}
