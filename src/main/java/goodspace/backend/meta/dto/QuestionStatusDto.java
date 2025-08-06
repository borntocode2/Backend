package goodspace.backend.meta.dto;

import goodspace.backend.qna.domain.QuestionStatus;
import lombok.Builder;

@Builder
public record QuestionStatusDto(
        String name,
        String korean
) {
    public static QuestionStatusDto from(QuestionStatus questionStatus) {
        return QuestionStatusDto.builder()
                .name(questionStatus.name())
                .korean(questionStatus.getKorean())
                .build();
    }
}
