package goodspace.backend.cart.service;

import goodspace.backend.cart.dto.CartItemAddRequestDto;
import goodspace.backend.cart.dto.CartItemInfoResponseDto;
import goodspace.backend.cart.dto.CartItemUpdateRequestDto;
import goodspace.backend.fixture.GoodSpaceUserFixture;
import goodspace.backend.fixture.ItemFixture;
import goodspace.backend.global.domain.Item;
import goodspace.backend.global.repository.CartItemRepository;
import goodspace.backend.global.repository.ItemRepository;
import goodspace.backend.cart.domain.CartItem;
import goodspace.backend.user.domain.User;
import goodspace.backend.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class CartItemServiceTest {
    static final Supplier<EntityNotFoundException> DTO_NOT_FOUND = () -> new EntityNotFoundException("DTO를 조회할 수 없습니다.");
    static final int DEFAULT_QUANTITY = 5;
    static final int NEW_QUANTITY = 9474747;

    @Autowired
    CartItemService cartItemService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    CartItemRepository cartItemRepository;

    User user;
    User emptyCartUser;
    Item itemA;
    Item itemB;
    CartItem cartItemA;
    CartItem cartItemB;
    List<CartItem> existCartItems;

    @BeforeEach
    void resetEntities() {
        user = userRepository.save(GoodSpaceUserFixture.A.getInstance());
        emptyCartUser = userRepository.save(GoodSpaceUserFixture.B.getInstance());

        itemA = itemRepository.save(ItemFixture.PUBLIC_A.getInstance());
        itemB = itemRepository.save(ItemFixture.PUBLIC_B.getInstance());

        cartItemA = cartItemRepository.save(CartItem.builder()
                .user(user)
                .item(itemA)
                .quantity(DEFAULT_QUANTITY)
                .build());
        cartItemB = cartItemRepository.save(CartItem.builder()
                .user(user)
                .item(itemB)
                .quantity(DEFAULT_QUANTITY)
                .build());

        user.addCartItem(cartItemA);
        user.addCartItem(cartItemB);
        existCartItems = List.of(cartItemA, cartItemB);
    }

    @Nested
    class getCartItems {
        @Test
        @DisplayName("회원의 모든 장바구니 상품을 조회한다")
        void getCartItemsOfUser() {
            List<CartItemInfoResponseDto> responseDtos = cartItemService.getCartItems(user.getId());

            assertThat(responseDtos.size()).isEqualTo(existCartItems.size());

            for (CartItem existCartItem : existCartItems) {
                CartItemInfoResponseDto responseDto = findDtoById(existCartItem.getId(), responseDtos);

                assertThat(isEqual(existCartItem, responseDto)).isTrue();
            }
        }
    }

    @Nested
    class addCartItem {
        @Test
        @DisplayName("새로운 장바구니 상품을 추가한다")
        void addNewCartItem() {
            // given
            CartItemAddRequestDto requestDto = CartItemAddRequestDto.builder()
                    .itemId(itemA.getId())
                    .quantity(DEFAULT_QUANTITY)
                    .build();

            // when
            cartItemService.addCartItem(emptyCartUser.getId(), requestDto);

            // then
            List<CartItem> cartItems = emptyCartUser.getCartItems();
            assertThat(cartItems.size()).isEqualTo(1);

            CartItem cartItem = cartItems.get(0);
            assertThat(cartItem.getItem()).isEqualTo(itemA);
            assertThat(cartItem.getUser()).isEqualTo(emptyCartUser);
        }
    }

    @Nested
    class updateCartItem {
        @Test
        @DisplayName("장바구니 상품의 정보(수량)를 수정한다")
        void updateInfoOfCartItem() {
            // given
            CartItemUpdateRequestDto requestDto = CartItemUpdateRequestDto.builder()
                    .cartItemId(cartItemA.getId())
                    .quantity(NEW_QUANTITY)
                    .build();

            // when
            cartItemService.updateCartItem(user.getId(), requestDto);

            // then
            assertThat(cartItemA.getQuantity()).isEqualTo(NEW_QUANTITY);
        }

        @ParameterizedTest
        @DisplayName("수량이 음수면 예외가 발생한다")
        @ValueSource(ints = {Integer.MIN_VALUE, -9999, -1})
        void updateInfoOfCartItem(int negativeQuantity) {
            CartItemUpdateRequestDto requestDto = CartItemUpdateRequestDto.builder()
                    .cartItemId(cartItemA.getId())
                    .quantity(negativeQuantity)
                    .build();

            assertThatThrownBy(() -> cartItemService.updateCartItem(user.getId(), requestDto))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class removeCartItem {
        @Test
        @DisplayName("회원의 장바구니 상품을 제거한다")
        void removeCartItemOfUser() {
            List<CartItem> cartItems = user.getCartItems();
            long existAmountOfCartItem = cartItems.size();

            cartItemService.removeCartItem(user.getId(), cartItemA.getId());

            long currentAmountOfCartITem = cartItems.size();
            assertThat(currentAmountOfCartITem).isEqualTo(existAmountOfCartItem - 1);
            assertThat(cartItems.contains(cartItemA)).isFalse();
        }
    }

    private CartItemInfoResponseDto findDtoById(long id, List<CartItemInfoResponseDto> dtos) {
        return dtos.stream()
                .filter(dto -> dto.id().equals(id))
                .findAny()
                .orElseThrow(DTO_NOT_FOUND);
    }

    private boolean isEqual(CartItem cartItem, CartItemInfoResponseDto dto) {
        return cartItem.getId().equals(dto.id()) &&
                cartItem.getQuantity().equals(dto.quantity()) &&
                isEqual(cartItem.getItem(), dto.item());
    }

    private boolean isEqual(Item item, CartItemInfoResponseDto.ItemDto dto) {
        return item.getName().equals(dto.name()) &&
                item.getPrice().equals(dto.price()) &&
                Objects.equals(item.getTitleImageUrl(), dto.titleImageUrl());
    }
}
