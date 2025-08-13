package goodspace.backend.shipping.service;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import goodspace.backend.shipping.dto.ShippingResponseDto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@Transactional
@RequiredArgsConstructor
public class ShippingTraceService {
    private final RestTemplate restTemplate;


    public ShippingResponseDto requestShippingStatus(String serviceKey, String rgist) throws Exception {
        String baseUrl = "http://openapi.epost.go.kr/trace/retrieveLongitudinalCombinedService/retrieveLongitudinalCombinedService/getLongitudinalCombinedList";

        // 서비스키와 등기번호를 URL에 직접 추가
        String urlStr = baseUrl + "?ServiceKey=" + URLEncoder.encode(serviceKey, StandardCharsets.UTF_8)
                + "&rgist=" + URLEncoder.encode(rgist, StandardCharsets.UTF_8);

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/xml"); // XML 요청

        // 응답 읽기
        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream(), StandardCharsets.UTF_8));
        }

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();

        // XML을 ShippingResponseDto로 변환
        XmlMapper xmlMapper = new XmlMapper();
        return xmlMapper.readValue(sb.toString(), ShippingResponseDto.class);
    }
}
