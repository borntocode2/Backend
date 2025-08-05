package goodspace.backend.qna.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class AnswerDto {
    private String content;
    private LocalDateTime createdAt;
}
