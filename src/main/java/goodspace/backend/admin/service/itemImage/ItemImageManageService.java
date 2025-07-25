package goodspace.backend.admin.service.itemImage;

import goodspace.backend.admin.dto.itemImage.ItemImageDeleteRequestDto;
import goodspace.backend.admin.dto.itemImage.ItemImageInfoResponseDto;
import goodspace.backend.admin.dto.itemImage.ItemImageRegisterRequestDto;

import java.util.List;

public interface ItemImageManageService {
    List<ItemImageInfoResponseDto> findByItem(long itemId);

    ItemImageInfoResponseDto register(ItemImageRegisterRequestDto requestDto);

    void delete(ItemImageDeleteRequestDto requestDto);
}
