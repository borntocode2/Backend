package goodspace.backend.domain.qna;

import goodspace.backend.domain.BaseEntity;
import goodspace.backend.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
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

    @ManyToOne
    private User user;

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public void setQuestionStatus(QuestionStatus questionStatus) {
        this.questionStatus = questionStatus;
    }
}
