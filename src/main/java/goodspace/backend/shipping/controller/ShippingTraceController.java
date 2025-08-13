package goodspace.backend.shipping.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import goodspace.backend.shipping.dto.ShippingResponseDto;
import goodspace.backend.shipping.service.ShippingTraceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/shipping")
@RequiredArgsConstructor
public class ShippingTraceController {
    private final ShippingTraceService shippingTraceService;

    @Value("${api.epost.shipping-service-key}")
    private String ServiceKey;

    private String baseUrl =
            "http://openapi.epost.go.kr/trace/retrieveLongitudinalCombinedService/retrieveLongitudinalCombinedService/getLongitudinalCombinedList";

    @GetMapping("/shippingDetatil")
    public String getShippingDetail(@RequestParam String rgist) {


        //TODO : url 뿌려주고 DTo받았으니 이 데이터를 활용해야함. 새로운 detail업데이트 and status업데이트
        try {
            String encodedServiceKey = URLEncoder.encode(ServiceKey, StandardCharsets.UTF_8.toString());
            String url = baseUrl + "?ServiceKey=" + encodedServiceKey + "&rgist=" + rgist;
            ShippingResponseDto shippingResponseDto = shippingTraceService.requestShippingStatus(url);
            System.out.println(url);
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(shippingResponseDto);
            System.out.println(json);

            return json;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "JSON 변환 중 오류 발생";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "인코딩 변환 실패";
        }

    }
}