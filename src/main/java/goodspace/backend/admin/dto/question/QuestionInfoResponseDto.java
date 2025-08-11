package goodspace.backend.admin.dto.question;

import goodspace.backend.qna.domain.Question;
import goodspace.backend.qna.domain.QuestionStatus;
import goodspace.backend.qna.domain.QuestionType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record QuestionInfoResponseDto(
    Long id,
    String title,
    String content,
    QuestionType questionType,
    QuestionStatus questionStatus,
    String userEmail,
    LocalDateTime createdAt
) {
    public static QuestionInfoResponseDto from(Question question) {
        return QuestionInfoResponseDto.builder()
                .id(question.getId())
                .title(question.getTitle())
                .content(question.getContent())
                .questionType(question.getQuestionType())
                .questionStatus(question.getQuestionStatus())
                .userEmail(question.getUserEmail())
                .createdAt(question.getCreatedAt())
                .build();
    }
}
