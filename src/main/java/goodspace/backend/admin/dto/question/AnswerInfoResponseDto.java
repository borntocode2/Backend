package goodspace.backend.admin.dto.question;

import goodspace.backend.qna.domain.Answer;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AnswerInfoResponseDto(
        Long id,
        String content,
        LocalDateTime updatedAt
) {
    public static AnswerInfoResponseDto from(Answer answer) {
        return AnswerInfoResponseDto.builder()
                .id(answer.getId())
                .content(answer.getContent())
                .updatedAt(answer.getUpdatedAt())
                .build();

    }
}
