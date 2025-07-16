package goodspace.backend.domain.qna;

import jakarta.persistence.*;
import org.aspectj.weaver.patterns.TypePatternQuestions;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Entity
public class Answer {
    @Id
    @GeneratedValue
    private Long id;
    private String content;

    private LocalDateTime createdAt;

    @OneToOne
    @JoinColumn(name = "question_id")
    private Question question;
}
