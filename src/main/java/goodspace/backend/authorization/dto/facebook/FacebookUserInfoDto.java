package goodspace.backend.authorization.dto.facebook;

import com.google.gson.annotations.SerializedName;
import goodspace.backend.user.domain.OAuthUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static goodspace.backend.user.domain.OAuthType.FACEBOOK;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FacebookUserInfoDto {
    @SerializedName("id")
    private String id;
    @SerializedName("email")
    private String email;

    public OAuthUser toEntity() {
        return OAuthUser.builder()
                .identifier(id)
                .email(email)
                .oauthType(FACEBOOK)
                .build();
    }
}
