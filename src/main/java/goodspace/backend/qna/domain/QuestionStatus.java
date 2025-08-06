package goodspace.backend.qna.domain;

import lombok.Getter;

@Getter
public enum QuestionStatus {
    WAITING("대기중"),
    COMPLETED("답변완료");

    private final String korean;

    QuestionStatus(String korean) {
        this.korean = korean;
    }
}
