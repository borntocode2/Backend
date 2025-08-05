package goodspace.backend.cart.dto;

import lombok.Builder;

@Builder
public record CartItemAddRequestDto(
        Long itemId,
        Integer quantity
) {
}
