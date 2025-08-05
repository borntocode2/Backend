package goodspace.backend.qna.domain;

import goodspace.backend.global.domain.BaseEntity;
import goodspace.backend.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionFile> questionFiles = new ArrayList<>();


    @ManyToOne
    private User user;

    public void setQuestionFiles(List<QuestionFile> questionFiles) {
        this.questionFiles = questionFiles;
    }
    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public void setQuestionStatus(QuestionStatus questionStatus) {
        this.questionStatus = questionStatus;
    }

    public void modifyQuestion(String title, String content, QuestionType questionType) {
        this.title = title;
        this.content = content;
        this.questionType = questionType;
    }
}
