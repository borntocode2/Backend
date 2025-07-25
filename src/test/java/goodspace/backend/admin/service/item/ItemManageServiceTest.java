package goodspace.backend.admin.service.item;

import goodspace.backend.admin.dto.item.ItemDeleteRequestDto;
import goodspace.backend.admin.dto.item.ItemInfoResponseDto;
import goodspace.backend.admin.dto.item.ItemRegisterRequestDto;
import goodspace.backend.admin.dto.item.ItemUpdateRequestDto;
import goodspace.backend.admin.image.ImageManager;
import goodspace.backend.admin.image.ImageManagerImpl;
import goodspace.backend.client.domain.Client;
import goodspace.backend.global.domain.Item;
import goodspace.backend.global.domain.ItemImage;
import goodspace.backend.fixture.ClientFixture;
import goodspace.backend.fixture.ImageFixture;
import goodspace.backend.fixture.ItemFixture;
import goodspace.backend.client.repository.ClientRepository;
import goodspace.backend.global.repository.ItemImageRepository;
import goodspace.backend.global.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemManageServiceTest {
    static final Supplier<EntityNotFoundException> DTO_NOT_FOUND = () -> new EntityNotFoundException("DTO가 조회되지 않습니다.");

    static final String DEFAULT_NAME = "defaultName";
    static final int DEFAULT_PRICE = 12345;
    static final String DEFAULT_SHORT_DESCRIPTION = "defaultShortDescription";
    static final String DEFAULT_LANDING_PAGE_DESCRIPTION = "defaultLandingPageDescription";

    static final String NEW_NAME = "newName";
    static final int NEW_PRICE = 98765;
    static final String NEW_SHORT_DESCRIPTION = "newShortDescription";
    static final String NEW_LANDING_PAGE_DESCRIPTION = "newLandingPageDescription";

    @TempDir
    Path basePath;
    ImageManager imageManager;

    final ItemManageService itemManageService;
    final ClientRepository clientRepository;
    final ItemRepository itemRepository;
    final ItemImageRepository itemImageRepository;

    Client client;
    Item itemA;
    Item itemB;
    ItemImage itemImageA;
    ItemImage itemImageB;
    List<Item> existItems;

    @BeforeEach
    void resetEntities() {
        imageManager = new ImageManagerImpl(basePath.toString());

        client = clientRepository.save(ClientFixture.CREATOR.getInstance());
        itemA = itemRepository.save(ItemFixture.A.getInstance());
        itemB = itemRepository.save(ItemFixture.B.getInstance());

        itemImageA = itemImageRepository.save(ItemImage.getEmptyInstance());
        itemImageB = itemImageRepository.save(ItemImage.getEmptyInstance());

        itemImageA.setImageUrl(imageManager.createImageUrl(itemA.getId(), itemImageA.getId(), ImageFixture.GDG.encodedImage));
        itemImageB.setImageUrl(imageManager.createImageUrl(itemB.getId(), itemImageB.getId(), ImageFixture.KOTLIN.encodedImage));

        client.addItems(List.of(itemA, itemB));
        existItems = List.of(itemA, itemB);
    }

    @Nested
    class findByClient {
        @Test
        @DisplayName("특정 클라이언트의 아이템을 모두 반환한다")
        void returnEveryItemOfClient() {
            List<ItemInfoResponseDto> itemDtos = itemManageService.findByClient(client.getId());

            assertThat(itemDtos.size()).isEqualTo(existItems.size());

            for (Item existItem : existItems) {
                ItemInfoResponseDto itemDto = findDtoById(existItem.getId(), itemDtos);
                assertThat(isEqual(existItem, itemDto)).isTrue();
            }
        }
    }

    @Nested
    class register {
        @Test
        @DisplayName("클라이언트에 상품을 추가한다")
        void addItemToClient() {
            // given
            ItemRegisterRequestDto requestDto = ItemRegisterRequestDto.builder()
                    .clientId(client.getId())
                    .name(DEFAULT_NAME)
                    .price(DEFAULT_PRICE)
                    .shortDescription(DEFAULT_SHORT_DESCRIPTION)
                    .landingPageDescription(DEFAULT_LANDING_PAGE_DESCRIPTION)
                    .build();

            // when
            ItemInfoResponseDto registeredItemDto = itemManageService.register(requestDto);

            // then
            assertThat(isEqual(requestDto, registeredItemDto)).isTrue();
            assertThat(isClientHasItem(client, registeredItemDto.id())).isTrue();
        }
    }

    @Nested
    class update {
        @Test
        @DisplayName("상품의 내용(이미지 제외)을 수정한다")
        void updateItem() {
            // given
            ItemUpdateRequestDto requestDto = ItemUpdateRequestDto.builder()
                    .clientId(client.getId())
                    .itemId(itemA.getId())
                    .name(NEW_NAME)
                    .price(NEW_PRICE)
                    .shortDescription(NEW_SHORT_DESCRIPTION)
                    .landingPageDescription(NEW_LANDING_PAGE_DESCRIPTION)
                    .build();

            // when
            itemManageService.update(requestDto);

            // then
            assertThat(isEqual(itemA, requestDto)).isTrue();
        }
    }

    @Nested
    class delete {
        @Test
        @DisplayName("상품을 삭제한다")
        void deleteItem() {
            // given
            ItemDeleteRequestDto requestDto = ItemDeleteRequestDto.builder()
                    .clientId(client.getId())
                    .itemId(itemA.getId())
                    .build();

            // when
            itemManageService.delete(requestDto);

            // then
            assertThat(isClientHasItem(client, itemA.getId())).isFalse();
        }
    }

    private ItemInfoResponseDto findDtoById(long id, List<ItemInfoResponseDto> dtos) {
        return dtos.stream()
                .filter(dto -> dto.id().equals(id))
                .findAny()
                .orElseThrow(DTO_NOT_FOUND);
    }

    private boolean isEqual(Item item, ItemInfoResponseDto dto) {
        Set<String> itemUrlSet = new HashSet<>(item.getImageUrls());
        Set<String> dtoUrlSet = new HashSet<>(dto.imageUrls());

        return item.getId().equals(dto.id()) &&
                item.getName().equals(dto.name()) &&
                item.getPrice().equals(dto.price()) &&
                item.getShortDescription().equals(dto.shortDescription()) &&
                item.getLandingPageDescription().equals(dto.landingPageDescription()) &&
                itemUrlSet.equals(dtoUrlSet);
    }

    private boolean isEqual(ItemRegisterRequestDto registerDto, ItemInfoResponseDto infoDto) {
        return registerDto.name().equals(infoDto.name()) &&
                registerDto.price().equals(infoDto.price()) &&
                registerDto.shortDescription().equals(infoDto.shortDescription()) &&
                registerDto.landingPageDescription().equals(infoDto.landingPageDescription());
    }

    private boolean isEqual(Item item, ItemUpdateRequestDto dto) {
        return item.getId().equals(dto.itemId()) &&
                item.getClient().getId().equals(dto.clientId()) &&
                item.getName().equals(dto.name()) &&
                item.getPrice().equals(dto.price()) &&
                item.getShortDescription().equals(dto.shortDescription()) &&
                item.getLandingPageDescription().equals(dto.landingPageDescription());
    }

    private boolean isClientHasItem(Client client, long itemId) {
        return client.getItems().stream()
                .anyMatch(item -> item.getId().equals(itemId));
    }
}
