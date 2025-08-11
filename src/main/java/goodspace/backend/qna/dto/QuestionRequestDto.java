package goodspace.backend.qna.dto;

import goodspace.backend.qna.domain.QuestionType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class QuestionRequestDto {
    private String title;
    private String content;
    private QuestionType type;
}
