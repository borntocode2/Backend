package goodspace.backend.cart.controller;

import goodspace.backend.cart.dto.CartItemAddRequestDto;
import goodspace.backend.cart.dto.CartItemInfoResponseDto;
import goodspace.backend.cart.dto.CartItemUpdateRequestDto;
import goodspace.backend.cart.service.CartItemService;
import goodspace.backend.global.principal.PrincipalUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(
        name = "장바구니 API",
        description = "장바구니 관리 관련 기능"
)
public class CartItemController {
    private final CartItemService cartItemService;
    private final PrincipalUtil principalUtil;

    @GetMapping
    @Operation(
            summary = "장바구니 상품 조회",
            description = "해당 회원의 모든 장바구니 상품을 조회합니다."
    )
    public ResponseEntity<List<CartItemInfoResponseDto>> findCartItems(Principal principal) {
        long userId = principalUtil.findIdFromPrincipal(principal);
        List<CartItemInfoResponseDto> response = cartItemService.getCartItems(userId);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(
            summary = "장바구니 상품 추가",
            description = "장바구니 상품을 추가합니다"
    )
    public ResponseEntity<Void> addCartItem(
            Principal principal,
            @RequestBody CartItemAddRequestDto requestDto
    ) {
        long userId = principalUtil.findIdFromPrincipal(principal);
        cartItemService.addCartItem(userId, requestDto);

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    @Operation(
            summary = "장바구니 상품 수정",
            description = "장바구니 상품의 정보를 수정합니다(수량 변경용)"
    )
    public ResponseEntity<Void> updateCartItem(
            Principal principal,
            @RequestBody CartItemUpdateRequestDto requestDto
    ) {
        long userId = principalUtil.findIdFromPrincipal(principal);
        cartItemService.updateCartItem(userId, requestDto);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    @Operation(
            summary = "장바구니 상품 제거",
            description = "장바구니 상품을 제거합니다."
    )
    public ResponseEntity<Void> removeCartItem(
            Principal principal,
            @RequestParam Long cartItemId
    ) {
        long userId = principalUtil.findIdFromPrincipal(principal);
        cartItemService.removeCartItem(userId, cartItemId);

        return ResponseEntity.noContent().build();
    }
}
