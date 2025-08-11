package goodspace.backend.cart.dto;

import goodspace.backend.global.domain.Item;
import goodspace.backend.cart.domain.CartItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record CartItemInfoResponseDto(
    Long cartItemId,
    Integer quantity,
    ItemDto item
) {
    public static CartItemInfoResponseDto from(CartItem cartItem) {
        return CartItemInfoResponseDto.builder()
                .cartItemId(cartItem.getId())
                .quantity(cartItem.getQuantity())
                .item(ItemDto.from(cartItem.getItem()))
                .build();
    }

    @Builder
    @Schema(name = "CartItemInfoResponseDto.ItemDto")
    public record ItemDto(
            Long itemId,
            String name,
            Integer price,
            String titleImageUrl
    ) {
        public static ItemDto from(Item item) {
            return ItemDto.builder()
                    .itemId(item.getId())
                    .name(item.getName())
                    .price(item.getPrice())
                    .titleImageUrl(item.getTitleImageUrl())
                    .build();
        }
    }
}
