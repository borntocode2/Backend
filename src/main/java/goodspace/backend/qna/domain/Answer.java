package goodspace.backend.qna.domain;

import goodspace.backend.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;


@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@SQLDelete(sql = "UPDATE answer SET deleted = true, deleted_at = NOW() WHERE id = ?")
public class Answer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private String content;

    @OneToOne
    @Setter
    @JoinColumn(name = "question_id")
    private Question question;
}
