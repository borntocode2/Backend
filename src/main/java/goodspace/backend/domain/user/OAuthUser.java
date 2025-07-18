package goodspace.backend.domain.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class OAuthUser extends User {
    private String identifier;
    @Enumerated
    private OAuthType oauthType;
}
