package goodspace.backend.client.service;

import goodspace.backend.client.dto.ClientBriefInfoResponseDto;
import goodspace.backend.client.dto.ClientDetailsResponseDto;
import goodspace.backend.client.domain.Client;
import goodspace.backend.client.dto.ClientItemInfoResponseDto;
import goodspace.backend.client.repository.ClientRepository;
import goodspace.backend.global.domain.Item;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private static final Supplier<EntityNotFoundException> CLIENT_NOT_FOUND = () -> new EntityNotFoundException("클라이언트를 찾지 못했습니다.");
    private static final Supplier<IllegalStateException> PRIVATE_CLIENT = () -> new IllegalStateException("공개되지 않은 클라이언트입니다.");
    private static final Supplier<EntityNotFoundException> ITEM_NOT_FOUND = () -> new EntityNotFoundException("상품을 찾지 못했습니다.");
    private static final Supplier<IllegalStateException> PRIVATE_ITEM = () -> new IllegalStateException("공개되지 않은 상품입니다.");

    private final ClientRepository clientRepository;

    @Override
    @Transactional(readOnly = true)
    public ClientDetailsResponseDto getDetails(long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(CLIENT_NOT_FOUND);

        throwExceptionIfPrivate(client);

        return ClientDetailsResponseDto.of(client, true);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientBriefInfoResponseDto> getPublicClients() {
        List<Client> clients = clientRepository.findAll();

        return clients.stream()
                .filter(Client::isPublic)
                .map(ClientBriefInfoResponseDto::from)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ClientItemInfoResponseDto getClientAndItem(long clientId, long itemId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(CLIENT_NOT_FOUND);
        Item item = findItemFromClient(client, itemId);

        throwExceptionIfPrivate(client);
        throwExceptionIfPrivate(item);

        return ClientItemInfoResponseDto.of(client, item);
    }

    private Item findItemFromClient(Client client, long itemId) {
        return client.getItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findAny()
                .orElseThrow(ITEM_NOT_FOUND);
    }

    private void throwExceptionIfPrivate(Client client) {
        if (!client.isPublic()) {
            throw PRIVATE_CLIENT.get();
        }
    }

    private void throwExceptionIfPrivate(Item item) {
        if (!item.isPublic()) {
            throw PRIVATE_ITEM.get();
        }
    }
}
