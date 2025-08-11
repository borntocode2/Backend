package goodspace.backend.client.domain;

import lombok.Getter;

@Getter
public enum RegisterStatus {
    PRIVATE("비공개"),
    PUBLIC("공개");

    private final String korean;

    RegisterStatus(String korean) {
        this.korean = korean;
    }
}
