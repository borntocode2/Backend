package goodspace.backend.meta.service;

import goodspace.backend.meta.dto.*;

import java.util.List;

public interface MetaService {
    List<ClientTypeDto> getClientTypes();

    List<OAuthTypeDto> getOauthTypes();

    List<OrderStatusDto> getOrderStatuses();

    List<QuestionStatusDto> getQuestionStatuses();

    List<QuestionTypeDto> getQuestionTypes();

    List<RegisterStatusDto> getRegisterStatuses();
}
