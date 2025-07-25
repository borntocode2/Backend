package goodspace.backend.domain.qna;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class QuestionResponseDto {
    private String title;
    private String content;
    private Long userId;
    private QuestionType type;
    private QuestionStatus status;
    private Answer answer;

    private List<Long> fileIds = new ArrayList<>();
}
