package goodspace.backend.admin.controller;

import goodspace.backend.admin.dto.item.ItemDeleteRequestDto;
import goodspace.backend.admin.dto.item.ItemInfoResponseDto;
import goodspace.backend.admin.dto.item.ItemRegisterRequestDto;
import goodspace.backend.admin.dto.item.ItemUpdateRequestDto;
import goodspace.backend.admin.service.item.ItemManageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/item")
@RequiredArgsConstructor
@Tag(
        name = "상품 관리 API(관리자 전용)",
        description = "상품 관리 관련 기능"
)
public class ItemManageController {
    private final ItemManageService itemManageService;

    @GetMapping
    @Operation(
            summary = "상품 목록 조회",
            description = "클라이언트의 상품 목록을 조회합니다."
    )
    public ResponseEntity<List<ItemInfoResponseDto>> getItemsByClient(@RequestParam Long clientId) {
        List<ItemInfoResponseDto> response = itemManageService.findByClient(clientId);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(
            summary = "상품 추가",
            description = "새로운 상품을 추가합니다."
    )
    public ResponseEntity<Void> createClient(@RequestBody ItemRegisterRequestDto requestDto) {
        itemManageService.register(requestDto);

        return ResponseEntity.ok().build();
    }

    @PutMapping
    @Operation(
            summary = "상품 수정",
            description = "상품의 정보를 수정합니다.(이미지 제외)"
    )
    public ResponseEntity<Void> updateClient(@RequestBody ItemUpdateRequestDto requestDto) {
        itemManageService.update(requestDto);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    @Operation(
            summary = "상품 제거",
            description = "상품을 제거합니다."
    )
    public ResponseEntity<Void> deleteClient(@RequestBody ItemDeleteRequestDto requestDto) {
        itemManageService.delete(requestDto);

        return ResponseEntity.ok().build();
    }
}
