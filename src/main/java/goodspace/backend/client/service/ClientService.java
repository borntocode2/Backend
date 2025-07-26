package goodspace.backend.client.service;

import goodspace.backend.client.dto.ClientBriefInfoResponseDto;
import goodspace.backend.client.dto.ClientDetailsResponseDto;

import java.util.List;

public interface ClientService {
    ClientDetailsResponseDto getDetails(long clientId);

    List<ClientBriefInfoResponseDto> getPublicClients();
}
