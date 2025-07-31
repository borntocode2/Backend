package goodspace.backend.admin.service;

import goodspace.backend.qna.domain.Answer;
import goodspace.backend.qna.domain.Question;
import goodspace.backend.qna.dto.AnswerDto;
import goodspace.backend.qna.repository.QuestionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class AdminQuestionService {
    private final QuestionRepository questionRepository;

    @Transactional
    public String registerAnswer(Long id, AnswerDto answerDto) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("답변을 저장하는 로직에서 해당 질문을 DB에서 찾지 못했습니다."));

        Answer answer = Answer.builder()
                .content(answerDto.getContent())
                .question(question)
                .build();

        question.setAnswer(answer);

        return "답변 등록이 완료되었습니다.";
    }
}
