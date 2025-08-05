package goodspace.backend.admin.dto.question;

import lombok.Builder;

@Builder
public record AnswerRegisterRequestDto(
        Long questionId,
        String content
) {
}
