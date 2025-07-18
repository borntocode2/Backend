package goodspace.backend.domain.qna;

import goodspace.backend.domain.BaseEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Question extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String content;

    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    @Enumerated(EnumType.STRING)
    private QuestionStatus questionStatus;

    @OneToOne(mappedBy = "question", cascade = CascadeType.ALL)
    private Answer answer;

    @OneToOne(mappedBy = "question", cascade = CascadeType.ALL)
    private QuestionFile questionFile;
}
