package goodspace.backend.cart.dto;

import lombok.Builder;

@Builder
public record CartItemUpdateRequestDto(
        Long cartItemId,
        Integer quantity
) {
}
