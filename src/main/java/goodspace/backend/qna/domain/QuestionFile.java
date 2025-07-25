package goodspace.backend.qna.domain;

import goodspace.backend.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@NoArgsConstructor
@Getter
public class QuestionFile extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Lob
    @Column(name = "data", columnDefinition = "LONGBLOB")
    private byte[] data;

    private String extension;
    private String mimeType;
    private String name;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
}
