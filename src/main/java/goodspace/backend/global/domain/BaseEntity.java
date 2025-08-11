package goodspace.backend.global.domain;

import goodspace.backend.global.soft.delete.SoftDeleteConstant;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PreRemove;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@SuperBuilder
@Getter
@FilterDef(
        name = SoftDeleteConstant.FILTER_NAME,
        parameters = @ParamDef(name = SoftDeleteConstant.FILTER_PARAM, type = Boolean.class)
)
@Filter(name = SoftDeleteConstant.FILTER_NAME, condition = SoftDeleteConstant.CONDITION)
public class BaseEntity {
    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = SoftDeleteConstant.DELETED_COLUMN, nullable = false)
    @Builder.Default
    private boolean deleted = false;

    private LocalDateTime deletedAt;

    @PreRemove
    private void onPreRemove() {
        this.deleted = true;
        this.deletedAt = LocalDateTime.now();
    }
}
