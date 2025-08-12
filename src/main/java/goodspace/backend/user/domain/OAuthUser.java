package goodspace.backend.user.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql =
        "UPDATE oauth_user " +
        "SET require_unique = 0 " +
        "WHERE id = ?")
@SQLRestriction("deleted = false")
public class OAuthUser extends User {
    @Column(nullable = false)
    private String identifier;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Getter
    private OAuthType oauthType;

    @Column(nullable = false)
    @Builder.Default
    private boolean requireUnique = true;
}
