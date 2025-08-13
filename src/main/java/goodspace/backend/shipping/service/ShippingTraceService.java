package goodspace.backend.shipping.service;

import goodspace.backend.shipping.dto.ShippingResponseDto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional
@RequiredArgsConstructor
public class ShippingTraceService {
    private final RestTemplate restTemplate;

    public ShippingResponseDto requestShippingStatus(String url){
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

        return restTemplate.getForObject(url, ShippingResponseDto.class);
    }
}
