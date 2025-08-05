package goodspace.backend.admin.dto.question;

import lombok.Builder;

@Builder
public record AnswerUpdateRequestDto(
        Long answerId,
        String content
) {
}
