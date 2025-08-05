package goodspace.backend.cart.service;

import goodspace.backend.cart.dto.CartItemAddRequestDto;
import goodspace.backend.cart.dto.CartItemInfoResponseDto;
import goodspace.backend.cart.dto.CartItemUpdateRequestDto;

import java.util.List;

public interface CartItemService {
    List<CartItemInfoResponseDto> getCartItems(long userId);

    void addCartItem(long userId, CartItemAddRequestDto requestDto);

    void updateCartItem(long userId, CartItemUpdateRequestDto requestDto);

    void removeCartItem(long userId, long cartItemId);
}
