package goodspace.backend.domain.qna;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Question {
    @Id
    @GeneratedValue
    private int id;
    private String title;
    private String content;

    @Enumerated(EnumType.STRING)
    private QuestionType questionType;

    @Enumerated(EnumType.STRING)
    private QuestionStatus questionStatus;

    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "question", cascade = CascadeType.ALL)
    private Answer answer;

    @OneToOne(mappedBy = "question", cascade = CascadeType.ALL)
    private QuestionFile questionFile;
}
