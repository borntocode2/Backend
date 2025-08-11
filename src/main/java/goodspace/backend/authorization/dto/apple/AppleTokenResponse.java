package goodspace.backend.authorization.dto.apple;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AppleTokenResponse {
    @SerializedName("access_token")
    String accessToken;
    @SerializedName("id_token")
    String idToken;
    @SerializedName("refresh_token")
    String refreshToken;
    @SerializedName("token_type")
    String tokenType;
    @SerializedName("expires_in")
    Long expiresIn;
}
