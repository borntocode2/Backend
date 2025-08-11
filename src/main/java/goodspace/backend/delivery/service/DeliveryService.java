package goodspace.backend.delivery.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import goodspace.backend.delivery.domain.DeliveryHistory;
import goodspace.backend.delivery.domain.DeliveryStatus;
import goodspace.backend.delivery.dto.DeliveryHistoryDto;
import goodspace.backend.delivery.dto.DeliveryResponseDto;
import goodspace.backend.order.domain.Order;
import goodspace.backend.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
@Service
@Transactional
public class DeliveryService {
    private final OrderRepository orderRepository;

    @Value("${api.epost.service-key}")
    String serviceKey;

    public DeliveryService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public String RegistrationMappingWithOrderIdAfterCallTrace(Long orderId, String rgist) throws JsonProcessingException {
        String url = "http://openapi.epost.go.kr/trace/retrieveLongitudinalService/retrieveLongitudinalService/getLongitudinalDomesticList"
                + "?rgist=" + rgist
                + "&serviceKey=" + serviceKey;

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        XmlMapper xmlMapper = new XmlMapper();

        // 최상위 DTO로 한번에 파싱
        DeliveryResponseDto  deliveryResponseDto = xmlMapper.readValue(response.getBody(), DeliveryResponseDto.class);

        List<DeliveryHistoryDto> historyList = deliveryResponseDto.getLongitudinalDomesticList();

        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalStateException("Order not found"));

        // DeliveryStatusDto 대신 LongitudinalDomesticListResponseDto에서 필요한 필드를 꺼내서 사용
        mapOrderWithResponseDto(order, deliveryResponseDto , historyList);

        if (order.getDeliveryHistorys() != null && !order.getDeliveryHistorys().isEmpty()) {
            return "save completed";
        } else {
            return deliveryResponseDto.getLongitudinalDomesticList().toString();
        }
    }

    private void mapOrderWithResponseDto(Order order, DeliveryResponseDto responseDto, List<DeliveryHistoryDto> deliveryHistoryDtos) {
        if (order != null && responseDto != null && deliveryHistoryDtos != null) {
            order.setDeliveryStatus(DeliveryStatus.builder()
                    .sender(responseDto.getSender())
                    .receiver(responseDto.getReceiver())
                    .completeDate(responseDto.getCompleteDate())
                    .status(responseDto.getStatus())
                    .postType(responseDto.getPostType())
                    .registrationNumber(responseDto.getRegistrationNumber())
                    .treatType(responseDto.getTreatType())
                    .build());

            for (DeliveryHistoryDto deliveryHistoryDto : deliveryHistoryDtos) {
                order.addDeliveryHistory(DeliveryHistory.builder()
                        .date(deliveryHistoryDto.getDate())
                        .time(deliveryHistoryDto.getTime())
                        .location(deliveryHistoryDto.getLocation())
                        .processStatus(deliveryHistoryDto.getProcessStatus())
                        .build());
            }
        }
    }
}