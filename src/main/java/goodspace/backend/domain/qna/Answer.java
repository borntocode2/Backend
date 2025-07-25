package goodspace.backend.domain.qna;

import goodspace.backend.domain.BaseEntity;
import jakarta.persistence.*;


@Entity
public class Answer extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String content;

    @OneToOne
    @JoinColumn(name = "question_id")
    private Question question;

    public void setQuestion(Question question) {
        this.question = question;
        if (question.getAnswer() != this) {
            question.setAnswer(this);
        }
    }
}
