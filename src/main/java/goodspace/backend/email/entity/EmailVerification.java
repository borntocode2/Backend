package goodspace.backend.email.entity;

import goodspace.backend.domain.BaseEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * 이메일 인증 코드 저장용 엔티티
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class EmailVerification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 인증 대상 이메일 (PK)
     */
    @Column(nullable = false, length = 254)
    private String email;

    /**
     * 발급된 인증 코드
     */
    @Column(nullable = false, length = 20)
    private String code;

    /**
     * 인증 코드 만료 시각
     */
    @Column(nullable = false)
    private LocalDateTime expiresAt;

    /**
     * 인증됨 여부
     */
    @Builder.Default
    private boolean isVerified = false;

    public boolean hasSameCode(String code) {
        return this.code.equals(code);
    }

    public boolean isExpired(LocalDateTime compare) {
        return this.expiresAt.isBefore(compare);
    }

    public void verify() {
        isVerified = true;
    }
}
