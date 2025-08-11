package goodspace.backend.user.domain;

import goodspace.backend.global.domain.BaseEntity;
import goodspace.backend.global.security.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

@Entity
@NoArgsConstructor
@SuperBuilder
@Getter
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@SQLDelete(sql = "UPDATE user_role SET deleted = true, deleted_at = NOW() WHERE id = ?")
public class UserRole extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;
}
