package goodspace.backend.user.domain;

import lombok.Getter;

@Getter
public enum OAuthType {
    GOOD_SPACE("굿스페이스"),
    APPLE("애플"),
    FACEBOOK("페이스북"),
    GOOGLE("구글"),
    KAKAO("카카오"),
    NAVER("네이버");

    private final String korean;

    OAuthType(String korean) {
        this.korean = korean;
    }
}
