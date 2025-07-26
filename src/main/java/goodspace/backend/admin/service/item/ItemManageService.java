package goodspace.backend.admin.service.item;

import goodspace.backend.admin.dto.item.*;

import java.util.List;

public interface ItemManageService {
    List<ItemInfoResponseDto> findByClient(long clientId);

    ItemInfoResponseDto register(ItemRegisterRequestDto requestDto);

    ItemInfoResponseDto update(ItemUpdateRequestDto requestDto);

    void delete(ItemDeleteRequestDto requestDto);
}
