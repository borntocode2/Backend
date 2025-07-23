package goodspace.backend.service;

import goodspace.backend.domain.Order;
import goodspace.backend.domain.client.Item;
import goodspace.backend.domain.user.User;
import goodspace.backend.dto.ItemList;
import goodspace.backend.dto.OrderRequestDto;
import goodspace.backend.dto.OrderResponseDto;
import goodspace.backend.repository.ItemRepository;
import goodspace.backend.repository.OrderRepository;
import goodspace.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

//        for (ItemList list : orderRequest.getItemLists()){
//            for(int i = 0; i < list.getQuantity(); i++){
//                order.addItem(itemRepository.findById(list.getItemId())
//                        .orElseThrow(() -> new IllegalArgumentException("Order엔티티에 Item을 매핑하는 Service과정에서 Item을 찾는 것을 실패했습니다.")));
//            }
//        }
        orderRepository.save(order);
    }
    // a 3,  b 4

    public OrderResponseDto findOrderByOrderId(String orderId) {
        Order order = orderRepository.findByApproveResult_OrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문을 찾을 수 없습니다."));

        List<ItemList> itemDtos = order.getItems().stream()
                .map(item -> ItemList.builder()
                        .itemId(item.getId())      // 필드 이름에 맞춰 변경
                        .price(item.getPrice())    // 정상
                        .quantity(item.getQuantity())              // 임시 값 (수량 정보가 없다면 기본값)
                        .build())
                .toList();
        return OrderResponseDto.builder()
                .itemLists(itemDtos)
                .amount(calculateAmount(order.getItems()))
                .orderStatus(order.getOrderStatus())
                .orderDate(order.getCreatedAt().toString())
                .build();
    }

    public OrderResponseDto findOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문을 찾을 수 없습니다."));
        List<ItemList> itemDtos = order.getItems().stream()
                .map(item -> ItemList.builder()
                        .itemId(item.getId())      // 필드 이름에 맞춰 변경
                        .price(item.getPrice())    // 정상
                        .quantity(item.getQuantity())              // 임시 값 (수량 정보가 없다면 기본값)
                        .build())
                .toList();
        return OrderResponseDto.builder()
                .itemLists(itemDtos)
                .amount(calculateAmount(order.getItems()))
                .orderStatus(order.getOrderStatus())
                .orderDate(order.getCreatedAt().toString())
                .build();
    }

    private Long calculateAmount(List<Item> items) {
        return items.stream()
                .mapToLong(Item::getPrice)
                .sum();
    }
}
