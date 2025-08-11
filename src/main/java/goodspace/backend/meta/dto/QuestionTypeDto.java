package goodspace.backend.meta.dto;

import goodspace.backend.qna.domain.QuestionType;
import lombok.Builder;

@Builder
public record QuestionTypeDto(
        String name,
        String korean
) {
    public static QuestionTypeDto from(QuestionType questionType) {
        return QuestionTypeDto.builder()
                .name(questionType.name())
                .korean(questionType.getKorean())
                .build();
    }
}
