package goodspace.backend.domain.qna;

import goodspace.backend.domain.BaseEntity;
import jakarta.persistence.*;
import org.aspectj.weaver.patterns.TypePatternQuestions;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Entity
public class Answer extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String content;

    @OneToOne
    @JoinColumn(name = "question_id")
    private Question question;
}
