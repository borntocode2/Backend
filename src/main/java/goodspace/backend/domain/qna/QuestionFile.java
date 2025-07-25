package goodspace.backend.domain.qna;

import goodspace.backend.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
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
