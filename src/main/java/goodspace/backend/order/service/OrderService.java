package goodspace.backend.order.service;

import goodspace.backend.order.domain.Order;
import goodspace.backend.order.domain.OrderCartItem;
import goodspace.backend.global.domain.Item;
import goodspace.backend.user.domain.User;
import goodspace.backend.order.dto.OrderCartItemDto;
import goodspace.backend.order.dto.OrderRequestDto;
import goodspace.backend.order.dto.OrderResponseDto;
import goodspace.backend.global.repository.ItemRepository;
import goodspace.backend.order.repository.OrderRepository;
import goodspace.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;

    public void saveOrder(OrderRequestDto orderRequest) {
        User user = userRepository.findById(orderRequest.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Order엔티티에 User를 매핑하는 Service과정에서 User를 찾는 것을 실패했습니다."));

        Order order = Order.builder()
                .user(user)
                .orderOutId(orderRequest.getOrderOutId())
                .build();

        List<OrderCartItem> orderCartItems = orderRequest.getOrderCartItemDtos().stream()
                .map(dto -> {
                    Item item = itemRepository.findById(dto.getItemId())
                            .orElseThrow(() -> new IllegalArgumentException("Item not found: " + dto.getItemId()));
                    return OrderCartItem.builder()
                            .item(item)
                            .quantity(dto.getQuantity().intValue())
                            .order(order)
                            .build();
                })
                .collect(Collectors.toList());

        order.setOrderCartItems(orderCartItems);

        orderRepository.save(order);
    }

    public OrderResponseDto findOrderByOrderId(String orderId) {
        Order order = orderRepository.findByApproveResult_OrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문을 찾을 수 없습니다."));

        List<OrderCartItemDto> cartItemDtos = order.getOrderCartItems().stream()
                .map(cartItem -> OrderCartItemDto.builder()
                        .itemId(cartItem.getItem().getId())
                        .quantity(cartItem.getQuantity())
                        .orderId(cartItem.getOrder().getId())
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
}
