package goodspace.backend.repository;

import goodspace.backend.domain.qna.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QnARepository extends JpaRepository<Question, Long> {
}
