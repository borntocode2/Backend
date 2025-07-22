package goodspace.backend.payment.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import goodspace.backend.payment.dto.PaymentVerifyResultDto;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Controller
public class NicePayController {
    @GetMapping("/payment")
    public String showPaymentPage() {
        try {
            return "nicepay";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @RequestMapping("/payment/verify")
    public String verifyPayment(@ModelAttribute PaymentVerifyResultDto paymentVerifyResultDto) throws JsonProcessingException {

        String tid = paymentVerifyResultDto.getTid(); // 추출된 tid

        System.out.println("tid" + tid);
        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        String clientKey = "S2_fb903ce81792411ab6c459ec3a2a82c6";
        String secretKey = "36e730803c7f45f3ae1462408083c94c";

        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((clientKey + ":" + secretKey).getBytes(StandardCharsets.UTF_8)));
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 바디에 amount 포함
        Map<String, Object> body = new HashMap<>();
        body.put("amount", paymentVerifyResultDto.getAmount());

        ObjectMapper mapper = new ObjectMapper();
        HttpEntity<String> request = new HttpEntity<>(mapper.writeValueAsString(body), headers);

        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<JsonNode> response = restTemplate.postForEntity(
                    "https://sandbox-api.nicepay.co.kr/v1/payments/" + tid,
                    request,
                    JsonNode.class
            );
            System.out.println("응답 : " + response.getBody());

            JsonNode responseNode = response.getBody();
            String resultCode = responseNode.get("resultCode").asText();
            paymentVerifyResultDto.setAuthResultMsg(responseNode.get("resultMsg").asText());

            System.out.println(responseNode.toPrettyString());

            if (resultCode.equalsIgnoreCase("0000")){
                //TODO
                //해당 결제 응답을 유저와 매핑하여 저장할 서비스 - 레포지토리 로직 필요
                // tid와 amout저장 후에 추후 환불로직에도 쓰여야 함.
            }
            else{

            }

        } catch (HttpClientErrorException e) {
            System.out.println("클라이언트 에러: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        } catch (HttpServerErrorException e) {
            System.out.println("서버 에러: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        }

        return "paymentVerifyResult";
    }

    @RequestMapping(value="/cancel")
    public String cancelDemo(){
        //TODO - 프론트엔드 측, 결제 도중 취소 버튼을 눌렀을 시, 보여줄 화면
        return "";}
}
