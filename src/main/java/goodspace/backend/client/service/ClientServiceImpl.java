package goodspace.backend.client.service;

import goodspace.backend.client.dto.ClientBriefInfoResponseDto;
import goodspace.backend.client.dto.ClientDetailsResponseDto;
import goodspace.backend.client.domain.Client;
import goodspace.backend.client.repository.ClientRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private static final Supplier<EntityNotFoundException> CLIENT_NOT_FOUND = () -> new EntityNotFoundException("클라이언트를 찾지 못했습니다.");

    private final ClientRepository clientRepository;

    @Override
    public ClientDetailsResponseDto getDetails(long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(CLIENT_NOT_FOUND);

        return ClientDetailsResponseDto.from(client);
    }

    @Override
    public List<ClientBriefInfoResponseDto> getClients() {
        List<Client> clients = clientRepository.findAll();

        return clients.stream()
                .map(ClientBriefInfoResponseDto::from)
                .toList();
    }
}
