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
    private static final Supplier<IllegalStateException> PRIVATE_CLIENT = () -> new IllegalStateException("공개되지 않은 클라이언트입니다.");

    private final ClientRepository clientRepository;

    @Override
    public ClientDetailsResponseDto getDetails(long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(CLIENT_NOT_FOUND);

        if (!client.isPublic()) {
            throw PRIVATE_CLIENT.get();
        }

        return ClientDetailsResponseDto.of(client, true);
    }

    @Override
    public List<ClientBriefInfoResponseDto> getPublicClients() {
        List<Client> clients = clientRepository.findAll();

        return clients.stream()
                .filter(Client::isPublic)
                .map(ClientBriefInfoResponseDto::from)
                .toList();
    }
}
