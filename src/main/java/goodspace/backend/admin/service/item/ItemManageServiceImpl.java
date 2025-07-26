package goodspace.backend.admin.service.item;

import goodspace.backend.admin.dto.item.ItemDeleteRequestDto;
import goodspace.backend.admin.dto.item.ItemInfoResponseDto;
import goodspace.backend.admin.dto.item.ItemRegisterRequestDto;
import goodspace.backend.admin.dto.item.ItemUpdateRequestDto;
import goodspace.backend.admin.image.ImageManager;
import goodspace.backend.client.domain.Client;
import goodspace.backend.client.domain.RegisterStatus;
import goodspace.backend.global.domain.Item;
import goodspace.backend.client.repository.ClientRepository;
import goodspace.backend.global.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class ItemManageServiceImpl implements ItemManageService {
    private static final Supplier<EntityNotFoundException> CLIENT_NOT_FOUND = () -> new EntityNotFoundException("클라이언트를 찾을 수 없습니다.");
    private static final Supplier<EntityNotFoundException> ITEM_NOT_FOUND = () -> new EntityNotFoundException("상품을 찾을 수 없습니다.");

    private final ImageManager imageManager;

    private final ClientRepository clientRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ItemInfoResponseDto> findByClient(long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(CLIENT_NOT_FOUND);

        return client.getItems().stream()
                .map(ItemInfoResponseDto::from)
                .toList();
    }

    @Override
    @Transactional
    public ItemInfoResponseDto register(ItemRegisterRequestDto requestDto) {
        Client client = clientRepository.findById(requestDto.clientId())
                .orElseThrow(CLIENT_NOT_FOUND);

        Item item = createItem(requestDto, RegisterStatus.PRIVATE);
        client.addItem(item);

        return ItemInfoResponseDto.from(itemRepository.save(item));
    }

    @Override
    @Transactional
    public ItemInfoResponseDto update(ItemUpdateRequestDto requestDto) {
        Client client = clientRepository.findById(requestDto.clientId())
                .orElseThrow(CLIENT_NOT_FOUND);
        Item item = findItemFromClient(client, requestDto.itemId());

        item.update(
                requestDto.name(),
                requestDto.price(),
                requestDto.shortDescription(),
                requestDto.landingPageDescription(),
                requestDto.status()
        );

        return ItemInfoResponseDto.from(item);
    }

    @Override
    @Transactional
    public void delete(ItemDeleteRequestDto requestDto) {
        Client client = clientRepository.findById(requestDto.clientId())
                .orElseThrow(CLIENT_NOT_FOUND);
        Item item = findItemFromClient(client, requestDto.itemId());

        for (String imageUrl : item.getImageUrls()) {
            imageManager.deleteImage(imageUrl);
        }

        item.removeEveryImages();
        client.removeItem(item);
    }

    private Item createItem(ItemRegisterRequestDto itemDto, RegisterStatus status) {
        return Item.builder()
                .name(itemDto.name())
                .price(itemDto.price())
                .shortDescription(itemDto.shortDescription())
                .landingPageDescription(itemDto.landingPageDescription())
                .status(status)
                .build();
    }

    private Item findItemFromClient(Client client, long itemId) {
        return client.getItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findAny()
                .orElseThrow(ITEM_NOT_FOUND);
    }
}
