package goodspace.backend.shipping.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import goodspace.backend.shipping.dto.ShippingDetailDto;
import goodspace.backend.shipping.dto.ShippingResponseDto;
import goodspace.backend.shipping.service.ShippingTraceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;


@RestController
@RequestMapping("/shipping")
@RequiredArgsConstructor
public class ShippingTraceController {
    private final ShippingTraceService shippingTraceService;

    @Value("${api.epost.shipping-service-key}")
    private String serviceKey;

    private String baseUrl =
            "http://openapi.epost.go.kr/trace/retrieveLongitudinalCombinedService/retrieveLongitudinalCombinedService/getLongitudinalCombinedList";

    @GetMapping("/shippingDetail")
    public ShippingResponseDto getShippingDetail(@RequestParam String rgist) throws Exception {
        return shippingTraceService.requestShippingStatus(serviceKey, rgist);
    }
}