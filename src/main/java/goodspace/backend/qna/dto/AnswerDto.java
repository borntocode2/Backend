package goodspace.backend.qna.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class AnswerDto {
    private String content;
    private LocalDateTime createdAt;
}
