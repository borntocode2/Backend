package goodspace.backend.fixture;

import goodspace.backend.qna.domain.Question;
import goodspace.backend.qna.domain.QuestionStatus;
import goodspace.backend.qna.domain.QuestionType;

public enum QuestionFixture {
    DELIVERY(
            "DELIVERY title",
            "DELIVERY content",
            QuestionType.DELIVERY
    ),
    ORDER(
            "ORDER title",
            "ORDER content",
            QuestionType.ORDER
    ),
    ITEM(
            "ITEM title",
            "ITEM content",
            QuestionType.ITEM
    ),
    A(
            "A title",
            "A content",
            QuestionType.DELIVERY
    ),
    B(
            "B title",
            "B content",
            QuestionType.ORDER
    ),
    C(
            "C title",
            "C content",
            QuestionType.ITEM
    );

    private final String title;
    private final String content;
    private final QuestionType type;

    QuestionFixture(String title, String content, QuestionType type) {
        this.title = title;
        this.content = content;
        this.type = type;
    }

    public Question getInstance() {
        return Question.builder()
                .title(title)
                .content(content)
                .questionType(type)
                .questionStatus(QuestionStatus.WAITING)
                .build();
    }
}
