package goodspace.backend.qna.dto;

import goodspace.backend.qna.domain.QuestionType;
import lombok.Getter;

@Getter
public class QuestionRequestDto {
    private String title;
    private String content;
    private QuestionType type;
}
