package goodspace.backend.qna.domain;

import goodspace.backend.global.domain.BaseEntity;
import goodspace.backend.user.domain.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Setter
    @Enumerated(EnumType.STRING)
    private QuestionStatus questionStatus;

    @OneToOne(mappedBy = "question", cascade = CascadeType.ALL)
    private Answer answer;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<QuestionFile> questionFiles = new ArrayList<>();

    @ManyToOne
    @Setter
    private User user;

    public void addQuestionFiles(List<QuestionFile> questionFiles) {
        this.questionFiles.addAll(questionFiles);

        for (QuestionFile questionFile : questionFiles) {
            questionFile.setQuestion(this);
        }
    }
    public void setAnswer(Answer answer) {
        this.answer = answer;
        answer.setQuestion(this);
    }

    public String getUserEmail() {
        return user.getEmail();
    }
}
