package goodspace.backend.qna.domain;

import lombok.Getter;

@Getter
public enum QuestionType {
    DELIVERY("배송"),
    ORDER("주문"),
    ITEM("상품");

    private final String korean;

    QuestionType(String korean) {
        this.korean = korean;
    }
}
