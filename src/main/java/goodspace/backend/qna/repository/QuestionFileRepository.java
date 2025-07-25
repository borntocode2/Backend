package goodspace.backend.qna.repository;

import goodspace.backend.qna.domain.QuestionFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionFileRepository extends JpaRepository<QuestionFile, Long> {
}
