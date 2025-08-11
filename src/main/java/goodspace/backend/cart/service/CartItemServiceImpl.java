package goodspace.backend.cart.service;

import goodspace.backend.cart.dto.CartItemAddRequestDto;
import goodspace.backend.cart.dto.CartItemInfoResponseDto;
import goodspace.backend.cart.dto.CartItemUpdateRequestDto;
import goodspace.backend.global.domain.Item;
import goodspace.backend.global.repository.CartItemRepository;
import goodspace.backend.global.repository.ItemRepository;
import goodspace.backend.user.domain.User;
import goodspace.backend.cart.domain.CartItem;
import goodspace.backend.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private static final Supplier<EntityNotFoundException> USER_NOT_FOUND = () -> new EntityNotFoundException("회원을 조회하지 못했습니다.");
    private static final Supplier<EntityNotFoundException> CART_ITEM_NOT_FOUND = () -> new EntityNotFoundException("장바구니 상품을 조회하지 못했습니다.");
    private static final Supplier<EntityNotFoundException> ITEM_NOT_FOUND = () -> new EntityNotFoundException("상품을 조회하지 못했습니다.");
    private static final Supplier<IllegalArgumentException> ILLEGAL_QUANTITY = () -> new IllegalArgumentException("수량은 0이하일 수 없습니다.");

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CartItemInfoResponseDto> getCartItems(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(USER_NOT_FOUND);

        return user.getCartItems().stream()
                .map(CartItemInfoResponseDto::from)
                .toList();
    }

    @Override
    @Transactional
    public void removeCartItem(long userId, long cartItemId) {
        User user = userRepository.findById(userId)
                .orElseThrow(USER_NOT_FOUND);
        CartItem cartItem = findCartItemById(user, cartItemId);

        cartItemRepository.delete(cartItem);
    }

    @Override
    @Transactional
    public void addCartItem(long userId, CartItemAddRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(USER_NOT_FOUND);
        Item item = itemRepository.findById(requestDto.itemId())
                .orElseThrow(ITEM_NOT_FOUND);

        CartItem cartItem = CartItem.builder()
                .user(user)
                .item(item)
                .quantity(requestDto.quantity())
                .build();
        user.addCartItem(cartItem);
    }

    @Override
    @Transactional
    public void updateCartItem(long userId, CartItemUpdateRequestDto requestDto) {
        if (requestDto.quantity() < 0) {
            throw ILLEGAL_QUANTITY.get();
        }

        User user = userRepository.findById(userId)
                .orElseThrow(USER_NOT_FOUND);
        CartItem cartItem = findCartItemById(user, requestDto.cartItemId());

        cartItem.updateQuantity(requestDto.quantity());
    }

    private CartItem findCartItemById(User user, long cartItemId) {
        return user.getCartItems().stream()
                .filter(item -> item.getId().equals(cartItemId))
                .findAny()
                .orElseThrow(CART_ITEM_NOT_FOUND);
    }
}
