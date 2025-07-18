package goodspace.backend.fixture;

import lombok.Getter;

@Getter
public enum EmailFixture {
    USER_A("A@naver.com"),
    USER_B("B@gmail.com"),
    USER_C("C@kakao.com"),
    USER_D("D@daum.net"),
    USER_E("E@hanmail.net");

    private final String email;

    EmailFixture(String email) {
        this.email = email;
    }
}
