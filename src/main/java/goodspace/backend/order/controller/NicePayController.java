package goodspace.backend.order.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import goodspace.backend.order.domain.PaymentApproveResult;
import goodspace.backend.order.dto.PaymentVerifyRequestDto;
import goodspace.backend.order.service.NicePayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class NicePayController {
    private final NicePayService nicePayService;

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
    public ResponseEntity<Map<String, String>> verifyPayment(@ModelAttribute PaymentVerifyRequestDto paymentVerifyResultDto, Model model) throws JsonProcessingException {

        model.addAttribute("paymentVerifyResultDto", paymentVerifyResultDto);

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

            if (resultCode.equalsIgnoreCase("0000")) {
                int tryCount = 5;

                while (tryCount > 0) {
                    try {
                        PaymentApproveResult result = mapper.treeToValue(responseNode, PaymentApproveResult.class);
                        nicePayService.MappingOrderWithPaymentApproveResult(result);
                        break;
                    } catch (IllegalArgumentException e) {
                        return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(Map.of("error", "IllegalArgumentException에러가 발생하였습니다.PaymentApproveResult와 order매핑에 실패하였습니다." + e.getMessage()));
                    } catch (Exception e) {
                        log.info("[info 에러 발생]", e.getMessage());
                        tryCount--;
                    }
                }

                if (tryCount == 0){
                    return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .body(Map.of("error", "IllegalArgumentException를 제외한 에러가 발생하였습니다. 상위 에러메세지를 확인하세요."));
                }
            }

            else {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Payment의 ResultCode가 0000이 아닙니다. 결제에 실패하였습니다."));
            }

        } catch (HttpClientErrorException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "클라이언트 에러 발생." + e.getMessage()));
        } catch (HttpServerErrorException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "서버에 에러 발생." + e.getMessage()));
        }

        return ResponseEntity
                .ok(Map.of("message", "결제에 성공했습니다."));
    }

    @RequestMapping(value="/cancel")
    public String cancelDemo(){
        //TODO - 프론트엔드 측, 결제 도중 취소 버튼을 눌렀을 시, 보여줄 화면
        return "";}
}
