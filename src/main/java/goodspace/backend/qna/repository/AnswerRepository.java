package goodspace.backend.qna.repository;

import goodspace.backend.qna.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
