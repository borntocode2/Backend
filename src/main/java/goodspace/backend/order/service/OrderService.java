package goodspace.backend.order.service;

import goodspace.backend.cart.domain.CartItem;
import goodspace.backend.global.domain.Item;
import goodspace.backend.global.repository.CartItemRepository;
import goodspace.backend.global.repository.ItemRepository;
import goodspace.backend.global.security.TokenProvider;
import goodspace.backend.order.domain.Order;
import goodspace.backend.order.domain.OrderCartItem;
import goodspace.backend.global.domain.Item;
import goodspace.backend.order.domain.OrdererInfo;
import goodspace.backend.user.domain.User;
import goodspace.backend.order.dto.OrderCartItemDto;
import goodspace.backend.order.dto.OrderRequestDto;
import goodspace.backend.order.dto.OrderResponseDto;
import goodspace.backend.order.repository.OrderRepository;
import goodspace.backend.user.domain.User;
import goodspace.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final CartItemRepository cartItemRepository;

    //TODO - error handling
    public Long saveOrder(Principal principal, OrderRequestDto orderRequest) {
        User user = userRepository.findById(TokenProvider.getUserIdFromPrincipal(principal))
                .orElseThrow(() -> new IllegalArgumentException("Order엔티티에 User를 매핑하는 Service과정에서 User를 찾는 것을 실패했습니다."));

        if (orderRequest.isRequireUpdateUserInfo()) {
            user.update(orderRequest.getOrderInfo());
        }

        Order order = Order.builder()
                .user(user)
                .deliveryInfo(orderRequest.getOrderInfo().toDeliveryInfo())
                .ordererInfo(OrdererInfo.builder()
                        .email(orderRequest.getOrderInfo().getEmail())
                        .phoneNumber(orderRequest.getOrderInfo().getPhoneNumber())
                        .name(orderRequest.getOrderInfo().getName())
                        .build())
                .build();

        List<OrderCartItem> orderCartItems = orderRequest.getOrderCartItemDtos().stream()
                .map(dto -> {
                    Item item = itemRepository.findById(dto.getItemId())
                            .orElseThrow(() -> new IllegalArgumentException("Item not found: " + dto.getItemId()));
                    return OrderCartItem.builder()
                            .item(item)
                            .quantity(dto.getQuantity())
                            .order(order)
                            .build();
                })
                .collect(toList());

        order.setOrderCartItems(orderCartItems);
        orderRepository.save(order);

        return order.getId();
    }

    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }

    public OrderResponseDto findOrderByOrderId(Long orderId) {
        Order order = orderRepository.findByApproveResult_OrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문을 찾을 수 없습니다."));

        List<OrderCartItemDto> cartItemDtos = order.getOrderCartItems().stream()
                .map(cartItem -> OrderCartItemDto.builder()
                        .itemId(cartItem.getItem().getId())
                        .quantity(cartItem.getQuantity())
                        .build()
                )
                .toList();

        Integer totalAmount = cartItemDtos.stream()
                .mapToInt(OrderCartItemDto::getAmount)
                .sum();

        return OrderResponseDto.builder()
                .orderId(order.getApproveResult().getOrderId())
                .amount((long)totalAmount)
                .orderCartItemDtos(cartItemDtos)
                .build();
    }

    private void removeCartItem(User user, List<OrderCartItemDto> orderCartItemDtos) {
        Map<Long, CartItem> cartItemMap = user.getCartItems().stream()
                .collect(toMap(CartItem::getItemId, cartItem -> cartItem));

        List<CartItem> itemsToDelete = orderCartItemDtos.stream()
                .map(dto -> cartItemMap.get(dto.getItemId()))
                .filter(Objects::nonNull)
                .toList();

        cartItemRepository.deleteAll(itemsToDelete);
    }
}
