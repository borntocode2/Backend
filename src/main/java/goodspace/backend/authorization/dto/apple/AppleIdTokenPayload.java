package goodspace.backend.authorization.dto.apple;

import com.google.gson.annotations.SerializedName;
import goodspace.backend.user.domain.OAuthType;
import goodspace.backend.user.domain.OAuthUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AppleIdTokenPayload {
    String iss;
    String sub; // 사용자 고유식별자
    String aud;
    Long iat;
    Long exp;
    String email;
    @SerializedName("email_verified")
    Boolean emailVerified;
    @SerializedName("is_private_email")
    Boolean isPrivateEmail;

    public OAuthUser toEntity() {
        return OAuthUser.builder()
                .identifier(sub)
                .email(email)
                .oauthType(OAuthType.APPLE)
                .build();
    }
}
