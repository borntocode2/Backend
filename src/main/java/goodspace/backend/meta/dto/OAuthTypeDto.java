package goodspace.backend.meta.dto;

import goodspace.backend.user.domain.OAuthType;
import lombok.Builder;

@Builder
public record OAuthTypeDto(
        String name,
        String korean
) {
    public static OAuthTypeDto from(OAuthType oauthType) {
        return OAuthTypeDto.builder()
                .name(oauthType.name())
                .korean(oauthType.getKorean())
                .build();
    }
}
