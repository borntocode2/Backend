package goodspace.backend.user.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "oauth_user_uq_identifier_oauth_type", columnNames = {"identifier", "oauth_type"})
})
public class OAuthUser extends User {
    @Column(nullable = false)
    private String identifier;

    @Enumerated
    @Column(nullable = false)
    @Getter
    private OAuthType oauthType;
}
