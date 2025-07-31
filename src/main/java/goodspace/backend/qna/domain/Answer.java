package goodspace.backend.qna.domain;

import goodspace.backend.global.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Entity
@SuperBuilder
@NoArgsConstructor
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
