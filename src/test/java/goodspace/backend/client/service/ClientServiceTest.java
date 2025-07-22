package goodspace.backend.client.service;

import goodspace.backend.client.dto.ClientBriefInfoResponseDto;
import goodspace.backend.client.dto.ClientDetailsResponseDto;
import goodspace.backend.client.dto.ItemBriefInfoResponseDto;
import goodspace.backend.domain.client.Client;
import goodspace.backend.domain.client.Item;
import goodspace.backend.fixture.ClientFixture;
import goodspace.backend.fixture.ItemFixture;
import goodspace.backend.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional
class ClientServiceTest {
    private final ClientService clientService;
    private final ClientRepository clientRepository;

    private Client clientA;
    private Client clientB;
    private Set<Client> clients;
    
    @BeforeEach
    void resetEntities() {
        Client client1 = ClientFixture.INFLUENCER.getInstance();
        Item item1 = ItemFixture.A.getInstance();
        client1.addItem(item1);

        Client client2 = ClientFixture.CREATOR.getInstance();
        Item item2 = ItemFixture.B.getInstance();
        client1.addItem(item2);

        this.clientA = clientRepository.save(client1);
        this.clientB = clientRepository.save(client2);
        this.clients = Set.of(clientA, clientB);
    }

    @Nested
    class getDetails {
        @Test
        @DisplayName("클라이언트의 상세 정보를 반환한다")
        void returnDetailsOfClient() {
            ClientDetailsResponseDto details = clientService.getDetails(clientA.getId());

            assertThat(isEqual(clientA, details)).isTrue();
        }
    }

    @Nested
    class getClients {
        @Test
        @DisplayName("클라이언트의 목록을 반환한다")
        void returnClients() {
            List<ClientBriefInfoResponseDto> clientDtos = clientService.getClients();

            assertThat(clientDtos.size() == clients.size()).isTrue();

            for (Client client : clients) {
                ClientBriefInfoResponseDto clientDto = clientDtos.stream()
                        .filter(dto -> dto.id().equals(client.getId()))
                        .findAny()
                        .orElseThrow(() -> new IllegalStateException("클라이언트와 아이디가 일치하는 DTO가 존재하지 않음"));

                assertThat(isEqual(client, clientDto)).isTrue();
            }
        }
    }

    private boolean isEqual(Client client, ClientDetailsResponseDto dto) {
        return client.getName().equals(dto.name()) &&
                client.getProfileImageUrl().equals(dto.profileImageUrl()) &&
                client.getBackgroundImageUrl().equals(dto.backgroundImageUrl()) &&
                client.getIntroduction().equals(dto.introduction()) &&
                isEqual(client.getItems(), dto.items());
    }

    private boolean isEqual(List<Item> items, List<ItemBriefInfoResponseDto> dtos) {
        Set<String> itemNameSet = toSet(items, Item::getName);
        Set<String> dtoNameSet = toSet(dtos, ItemBriefInfoResponseDto::name);

        Set<String> itemDescriptionSet = toSet(items, Item::getLandingPageDescription);
        Set<String> dtoDescriptionSet = toSet(dtos, ItemBriefInfoResponseDto::landingPageDescription);

        Set<String> itemUrlSet = items.stream()
                .map(Item::getImageUrls)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        Set<String> dtoUrlSet = dtos.stream()
                .map(ItemBriefInfoResponseDto::imageUrls)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        return itemNameSet.equals(dtoNameSet) &&
                itemDescriptionSet.equals(dtoDescriptionSet) &&
                itemUrlSet.equals(dtoUrlSet);
    }

    private boolean isEqual(Client client, ClientBriefInfoResponseDto dto) {
        return client.getId().equals(dto.id()) &&
                client.getName().equals(dto.name()) &&
                client.getProfileImageUrl().equals(dto.profileImageUrl()) &&
                client.getClientType() == dto.clientType();
    }

    private <E, T> Set<T> toSet(List<E> list, Function<E, T> function) {
        return list.stream()
                .map(function)
                .collect(Collectors.toSet());
    }
}
