package goodspace.backend.fixture;

import goodspace.backend.qna.domain.Answer;
import goodspace.backend.qna.domain.Question;

public enum AnswerFixture {
    A(
            "A content"
    );

    private final String content;

    AnswerFixture(
            String content
    ) {
        this.content = content;
    }

    public Answer getInstanceWith(Question question) {
        return Answer.builder()
                .content(content)
                .question(question)
                .build();
    }
}
