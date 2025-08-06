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

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
public class Question extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionType questionType;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private QuestionStatus questionStatus = QuestionStatus.WAITING;

    @OneToOne(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private Answer answer;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<QuestionFile> questionFiles = new ArrayList<>();

    @ManyToOne
    @JoinColumn(nullable = false)
    @Setter
    private User user;

    public void clearQuestionFiles() {
        for (QuestionFile questionFile : questionFiles) {
            questionFile.setQuestion(null);
        }

        questionFiles.clear();
    }

    public void addQuestionFiles(List<QuestionFile> questionFiles) {
        this.questionFiles.clear();

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

    public void modifyQuestion(String title, String content, QuestionType questionType) {
        this.title = title;
        this.content = content;
        this.questionType = questionType;
    }
}
