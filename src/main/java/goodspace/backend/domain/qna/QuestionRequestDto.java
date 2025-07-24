package goodspace.backend.domain.qna;

import lombok.Getter;

@Getter
public class QuestionRequestDto {
    private String title;
    private String content;
    private QuestionType type;
}
