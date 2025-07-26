package goodspace.backend.client.service;

import goodspace.backend.client.dto.ClientBriefInfoResponseDto;
import goodspace.backend.client.dto.ClientDetailsResponseDto;
import goodspace.backend.client.dto.ItemBriefInfoResponseDto;
import goodspace.backend.client.domain.Client;
import goodspace.backend.global.domain.Item;
import goodspace.backend.fixture.ClientFixture;
import goodspace.backend.fixture.ItemFixture;
import goodspace.backend.client.repository.ClientRepository;
import goodspace.backend.global.repository.ItemRepository;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional
class ClientServiceTest {
    private final ClientService clientService;
    private final ClientRepository clientRepository;
    private final ItemRepository itemRepository;

    private Client clientA;
    private Client clientB;
    private Client hasItemClient;
    private Client privateClient;
    private Set<Client> publicClients;
    private Set<Item> publicItems;

    @BeforeEach
    void resetEntities() {
        clientA = clientRepository.save(ClientFixture.INFLUENCER.getInstance());
        clientB = clientRepository.save(ClientFixture.CREATOR.getInstance());
        hasItemClient = clientRepository.save(ClientFixture.SINGER.getInstance());
        privateClient = clientRepository.save(ClientFixture.PRIVATE_CLIENT.getInstance());

        Item itemA = itemRepository.save(ItemFixture.PUBLIC_A.getInstance());
        Item itemB = itemRepository.save(ItemFixture.PUBLIC_B.getInstance());
        Item notReadyItem = itemRepository.save(ItemFixture.PRIVATE_A.getInstance());

        hasItemClient.addItem(itemA);
        hasItemClient.addItem(itemB);
        hasItemClient.addItem(notReadyItem);

        publicClients = Set.of(clientA, clientB, hasItemClient);
        publicItems = Set.of(itemA, itemB);
    }

    @Nested
    class getDetails {
        @Test
        @DisplayName("클라이언트의 상세 정보를 반환한다")
        void returnDetailsOfClient() {
            ClientDetailsResponseDto clientDetails = clientService.getDetails(clientA.getId());

            assertThat(isEqualWithoutItem(clientA, clientDetails)).isTrue();
        }

        @Test
        @DisplayName("클라이언트의 공개된 상품들을 반환한다")
        void returnItemsOfClient() {
            ClientDetailsResponseDto clientDetails = clientService.getDetails(hasItemClient.getId());

            assertThat(isEqual(publicItems, clientDetails.items())).isTrue();
        }

        @Test
        @DisplayName("공개되지 않은 클라이언트라면 예외를 던진다")
        void ifNotReadyClientThenThrowException() {
            assertThatThrownBy(() -> clientService.getDetails(privateClient.getId()))
                    .isInstanceOf(IllegalStateException.class);
        }
    }

    @Nested
    class getClients {
        @Test
        @DisplayName("공개된 클라이언트의 목록을 반환한다")
        void returnClients() {
            List<ClientBriefInfoResponseDto> clientDtos = clientService.getPublicClients();

            assertThat(clientDtos.size() == publicClients.size()).isTrue();

            for (Client client : publicClients) {
                ClientBriefInfoResponseDto clientDto = clientDtos.stream()
                        .filter(dto -> dto.id().equals(client.getId()))
                        .findAny()
                        .orElseThrow(() -> new IllegalStateException("클라이언트와 아이디가 일치하는 DTO가 존재하지 않음"));

                assertThat(isEqual(client, clientDto)).isTrue();
            }
        }
    }

    private boolean isEqualWithoutItem(Client client, ClientDetailsResponseDto dto) {
        return client.getName().equals(dto.name()) &&
                client.getProfileImageUrl().equals(dto.profileImageUrl()) &&
                client.getBackgroundImageUrl().equals(dto.backgroundImageUrl()) &&
                client.getIntroduction().equals(dto.introduction());
    }

    private boolean isEqual(Set<Item> items, List<ItemBriefInfoResponseDto> dtos) {
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

        System.out.println("itemNameSet = " + itemNameSet);
        System.out.println("dtoNameSet = " + dtoNameSet);
        System.out.println("itemDescriptionSet = " + itemDescriptionSet);
        System.out.println("dtoDescriptionSet = " + dtoDescriptionSet);
        System.out.println("itemUrlSet = " + itemUrlSet);
        System.out.println("dtoUrlSet = " + dtoUrlSet);

        System.out.println("itemNameSet.equals(dtoNameSet) = " + itemNameSet.equals(dtoNameSet));
        System.out.println("itemDescriptionSet.equals(dtoDescriptionSet) = " + itemDescriptionSet.equals(dtoDescriptionSet));
        System.out.println("itemUrlSet.equals(dtoUrlSet) = " + itemUrlSet.equals(dtoUrlSet));

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

    private <E, T> Set<T> toSet(Collection<E> collection, Function<E, T> function) {
        return collection.stream()
                .map(function)
                .collect(Collectors.toSet());
    }
}
