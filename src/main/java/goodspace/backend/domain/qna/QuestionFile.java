package goodspace.backend.domain.qna;

import jakarta.persistence.*;

@Entity
public class QuestionFile {
    @Id
    @GeneratedValue
    private int id;

    private byte[] data;
    private String extension;
    private String mineType;
    private String name;

    @OneToOne
    @JoinColumn(name = "question_id")
    private Question question;
}
