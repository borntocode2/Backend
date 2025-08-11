package goodspace.backend.meta.service;

import goodspace.backend.client.domain.ClientType;
import goodspace.backend.client.domain.RegisterStatus;
import goodspace.backend.meta.dto.*;
import goodspace.backend.order.domain.OrderStatus;
import goodspace.backend.qna.domain.QuestionStatus;
import goodspace.backend.qna.domain.QuestionType;
import goodspace.backend.user.domain.OAuthType;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class MetaServiceImpl implements MetaService {
    @Override
    public List<ClientTypeDto> getClientTypes() {
        return Arrays.stream(ClientType.values())
                .map(ClientTypeDto::from)
                .toList();
    }

    @Override
    public List<OAuthTypeDto> getOauthTypes() {
        return Arrays.stream(OAuthType.values())
                .map(OAuthTypeDto::from)
                .toList();
    }

    @Override
    public List<OrderStatusDto> getOrderStatuses() {
        return Arrays.stream(OrderStatus.values())
                .map(OrderStatusDto::from)
                .toList();
    }

    @Override
    public List<QuestionStatusDto> getQuestionStatuses() {
        return Arrays.stream(QuestionStatus.values())
                .map(QuestionStatusDto::from)
                .toList();
    }

    @Override
    public List<QuestionTypeDto> getQuestionTypes() {
        return Arrays.stream(QuestionType.values())
                .map(QuestionTypeDto::from)
                .toList();
    }

    @Override
    public List<RegisterStatusDto> getRegisterStatuses() {
        return Arrays.stream(RegisterStatus.values())
                .map(RegisterStatusDto::from)
                .toList();
    }
}
