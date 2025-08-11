package goodspace.backend.client.domain;

import lombok.Getter;

@Getter
public enum ClientType {
    CREATOR("크리에이터"),
    INFLUENCER("인플루언서");

    private final String korean;

    ClientType(String korean) {
        this.korean = korean;
    }
}
