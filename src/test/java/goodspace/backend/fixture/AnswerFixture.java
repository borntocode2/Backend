package goodspace.backend.fixture;

import goodspace.backend.qna.domain.Answer;

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

    public Answer getInstance() {
        return Answer.builder()
                .content(content)
                .build();
    }
}
