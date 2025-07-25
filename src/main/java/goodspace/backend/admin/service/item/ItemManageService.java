package goodspace.backend.admin.service.item;

import goodspace.backend.admin.dto.item.ItemDeleteRequestDto;
import goodspace.backend.admin.dto.item.ItemInfoResponseDto;
import goodspace.backend.admin.dto.item.ItemRegisterRequestDto;
import goodspace.backend.admin.dto.item.ItemUpdateRequestDto;

import java.util.List;

public interface ItemManageService {
    List<ItemInfoResponseDto> findByClient(long clientId);

    ItemInfoResponseDto register(ItemRegisterRequestDto requestDto);

    ItemInfoResponseDto update(ItemUpdateRequestDto requestDto);

    void delete(ItemDeleteRequestDto requestDto);
}
