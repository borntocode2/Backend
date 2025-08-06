package goodspace.backend.cart.dto;

import goodspace.backend.global.domain.Item;
import goodspace.backend.cart.domain.CartItem;
import lombok.Builder;

@Builder
public record CartItemInfoResponseDto(
    Long id,
    Integer quantity,
    ItemDto item
) {
    public static CartItemInfoResponseDto from(CartItem cartItem) {
        return CartItemInfoResponseDto.builder()
                .id(cartItem.getId())
                .quantity(cartItem.getQuantity())
                .item(ItemDto.from(cartItem.getItem()))
                .build();
    }

    @Builder
    public record ItemDto(
            String name,
            Integer price,
            String titleImageUrl
    ) {
        public static ItemDto from(Item item) {
            return ItemDto.builder()
                    .name(item.getName())
                    .price(item.getPrice())
                    .titleImageUrl(item.getTitleImageUrl())
                    .build();
        }
    }
}
