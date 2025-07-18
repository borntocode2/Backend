package goodspace.backend.email.entity;

import goodspace.backend.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

    /**
     * 인증 대상 이메일 (PK)
     */
    @Id
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
