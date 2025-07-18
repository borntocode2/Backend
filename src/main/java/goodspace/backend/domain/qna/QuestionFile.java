package goodspace.backend.domain.qna;

import goodspace.backend.domain.BaseEntity;
import jakarta.persistence.*;

@Entity
public class QuestionFile extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    private byte[] data;
    private String extension;
    private String mineType;
    private String name;

    @OneToOne
    @JoinColumn(name = "question_id")
    private Question question;
}
