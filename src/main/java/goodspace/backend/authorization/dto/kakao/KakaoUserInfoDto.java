package goodspace.backend.authorization.dto.kakao;

import com.google.gson.annotations.SerializedName;
import goodspace.backend.user.domain.OAuthUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static goodspace.backend.user.domain.OAuthType.KAKAO;

/**
 * 참고 문서: <a href="https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#req-user-info">...</a>
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class KakaoUserInfoDto {
    private String id;

    @SerializedName("kakao_account")
    private KakaoAccount kakaoAccount;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    static class KakaoAccount {
        @SerializedName("email_needs_agreement")
        private Boolean emailNeedsAgreement;
        @SerializedName("is_email_valid")
        private Boolean isEmailValid;
        @SerializedName("is_email_verified")
        private Boolean getIsEmailVerified;
        private String email;
    }

    public String getEmail() {
        return this.kakaoAccount.email;
    }

    public Boolean isValidatedEmail() {
        if (kakaoAccount.isEmailValid == null || kakaoAccount.getIsEmailVerified == null) {
            return false;
        }

        return kakaoAccount.isEmailValid && kakaoAccount.getIsEmailVerified;
    }

    public OAuthUser toEntity() {
            return OAuthUser.builder()
                    .identifier(id)
                    .email(kakaoAccount.email)
                    .oauthType(KAKAO)
                    .build();
    }
}
