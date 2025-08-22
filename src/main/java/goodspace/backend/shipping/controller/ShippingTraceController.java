package goodspace.backend.shipping.controller;

import goodspace.backend.shipping.dto.ShippingResponseDto;
import goodspace.backend.shipping.service.ShippingTraceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shipping")
@RequiredArgsConstructor
public class ShippingTraceController {
    private final ShippingTraceService shippingTraceService;

    @Value("${api.epost.shipping-service-key}")
    private String serviceKey;

    @GetMapping
    public ShippingResponseDto getShippingDetail(@RequestParam String rgist) throws Exception {
        return shippingTraceService.requestShippingStatus(serviceKey, rgist);
    }
}