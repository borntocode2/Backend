package goodspace.backend.admin.controller;

import goodspace.backend.admin.dto.itemImage.ItemImageDeleteRequestDto;
import goodspace.backend.admin.dto.itemImage.ItemImageRegisterRequestDto;
import goodspace.backend.admin.dto.itemImage.TitleImageUpdateRequestDto;
import goodspace.backend.admin.dto.itemImage.TotalItemImageResponseDto;
import goodspace.backend.admin.service.itemImage.ItemImageManageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/admin/item/image")
@RequiredArgsConstructor
@Tag(
        name = "상품 이미지 관리 API(관리자 전용)",
        description = "상품 이미지 관리 관련 기능"
)
public class ItemImageManageController {
    private final ItemImageManageService itemImageManageService;

    @GetMapping
    @Operation(
            summary = "상품 이미지 목록 조회",
            description = "상품의 이미지 목록을 조회합니다."
    )
    public ResponseEntity<TotalItemImageResponseDto> getItemsByClient(@RequestParam Long itemId) {
        TotalItemImageResponseDto response = itemImageManageService.findByItem(itemId);

        return ResponseEntity.ok(response);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "상품 이미지 추가",
            description = "상품에 이미지를 추가합니다."
    )
    public ResponseEntity<Void> addImage(
            @RequestParam Long clientId,
            @RequestParam Long itemId,
            @RequestPart("image") MultipartFile image
    ) {
        itemImageManageService.register(ItemImageRegisterRequestDto.builder()
                .clientId(clientId)
                .itemId(itemId)
                .image(image)
                .build());

        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/title", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "상품 타이틀 이미지 추가",
            description = "상품에 타이틀 이미지를 추가합니다."
    )
    public ResponseEntity<Void> addTitleImage(
            @RequestParam Long clientId,
            @RequestParam Long itemId,
            @RequestPart("image") MultipartFile image
    ) {
        itemImageManageService.registerTitleImage(ItemImageRegisterRequestDto.builder()
                .clientId(clientId)
                .itemId(itemId)
                .image(image)
                .build());

        return ResponseEntity.ok().build();
    }

    @PutMapping(path = "/title", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "상품 타이틀 이미지 수정",
            description = "상품의 타이틀 이미지를 수정합니다."
    )
    public ResponseEntity<Void> updateTitleImage(
            @RequestParam Long itemId,
            @RequestPart("image") MultipartFile image
    ) {
        itemImageManageService.updateTitleImage(TitleImageUpdateRequestDto.builder()
                .itemId(itemId)
                .image(image)
                .build());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    @Operation(
            summary = "상품 이미지 제거",
            description = "상품의 특정 이미지를 제거합니다."
    )
    public ResponseEntity<Void> deleteImage(@RequestBody ItemImageDeleteRequestDto requestDto) {
        itemImageManageService.delete(requestDto);

        return ResponseEntity.ok().build();
    }
}
