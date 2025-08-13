package goodspace.backend.shipping.controller;

import goodspace.backend.shipping.dto.ShippingResponseDto;
import goodspace.backend.shipping.service.ShippingTraceService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/shipping")
public class ShippingTraceController {
    private ShippingTraceService shippingTraceService;

    @Value("${api.epost.shipping-service-key}")
    private String serviceKey;

    private final String baseUrl =
            "http://openapi.epost.go.kr/trace/retrieveLongitudinalCombinedService/retrieveLongitudinalCombinedService/getLongitudinalCombinedList";

    @GetMapping("/shippingDetatil")
    public String getShippingDetail(@RequestParam String rgist) {
        String url = baseUrl + "?ServiceKey=" + serviceKey + "&rgist=" + rgist;
        ShippingResponseDto shippingResponseDto = shippingTraceService.requestShippingStatus(url);

        //TODO : url 뿌려주고 DTo받았으니 이 데이터를 활용해야함. 새로운 detail업데이트 and status 업데이트


    }
}
