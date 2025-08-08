package goodspace.backend.admin.dto.user;

import goodspace.backend.global.security.Role;
import goodspace.backend.user.domain.*;
import lombok.Builder;

import java.util.List;

@Builder
public record UserInfoDto(
        Long id,
        String name,
        Integer dateOfBirth,
        String email,
        String phoneNumber,
        List<Role> roles,
        DeliveryInfo deliveryInfo,
        OAuthType oauthType
) {
    public static UserInfoDto from(User user) {
        return UserInfoDto.builder()
                .id(user.getId())
                .name(user.getName())
                .dateOfBirth(user.getDateOfBirth())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .roles(user.getRoles())
                .deliveryInfo(user.getDeliveryInfo())
                .oauthType(user instanceof OAuthUser ? ((OAuthUser) user).getOauthType() : OAuthType.GOOD_SPACE)
                .build();
    }
}
