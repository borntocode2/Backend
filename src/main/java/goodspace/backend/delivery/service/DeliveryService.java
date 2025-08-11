package goodspace.backend.delivery.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import goodspace.backend.delivery.domain.DeliveryHistory;
import goodspace.backend.delivery.domain.DeliveryStatus;
import goodspace.backend.delivery.dto.DeliveryHistoryDto;
import goodspace.backend.delivery.dto.DeliveryStatusDto;
import goodspace.backend.delivery.dto.OnlyHistoryWrapper;
import goodspace.backend.order.domain.Order;
import goodspace.backend.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
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

        ObjectMapper xmlMapper = new XmlMapper();

        DeliveryStatusDto deliveryStatusDto = xmlMapper.readValue(response.getBody(), DeliveryStatusDto.class);
        OnlyHistoryWrapper parsed = xmlMapper.readValue(response.getBody(), OnlyHistoryWrapper.class);
        List<DeliveryHistoryDto> historyList = parsed.getLongitudinalDomesticList();

        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalStateException("Order not found"));

        orderMappingWithDeliveryDtos(order, deliveryStatusDto, historyList);

        if (order.getDeliveryHistorys() != null && !order.getDeliveryHistorys().isEmpty()) {
            return "save completed";
        }

        else{
            return "save failed";
        }
    }


    private void orderMappingWithDeliveryDtos(Order order, DeliveryStatusDto deliveryStatusDto, List<DeliveryHistoryDto> deliveryHistoryDtos) throws JsonProcessingException {
        if (order != null && deliveryStatusDto != null && deliveryHistoryDtos != null) {
            order.setDeliveryStatus(DeliveryStatus.builder()
                            .sender(deliveryStatusDto.getSender())
                            .receiver(deliveryStatusDto.getReceiver())
                            .completeDate(deliveryStatusDto.getCompleteDate())
                            .status(deliveryStatusDto.getStatus())
                            .postType(deliveryStatusDto.getPostType())
                            .registrationNumber(deliveryStatusDto.getRegistrationNumber())
                            .treatType(deliveryStatusDto.getTreatType())
                    .build());

            for(DeliveryHistoryDto deliveryHistoryDto : deliveryHistoryDtos) {
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




