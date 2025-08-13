package goodspace.backend.order.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import goodspace.backend.order.domain.PaymentApproveResult;
import goodspace.backend.order.dto.OrderResultMessageDto;
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

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/payment")
public class NicePayController {
    private final NicePayService nicePayService;

    @GetMapping(value = "/html", produces = MediaType.TEXT_HTML_VALUE)
    public String showPaymentPage(@RequestParam int amount,
                                  @RequestParam String goodsName,
                                  @RequestParam String orderId
    ) {

        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>NicePay 결제</title>\n" +
                "    <script src=\"https://pay.nicepay.co.kr/v1/js/\"></script>\n" +
                "</head>\n" +
                "<body onload=\"serverAuth()\">\n" +
                "\n" +
                "<script>\n" +
                "    function serverAuth() {\n" +
                "        AUTHNICE.requestPay({\n" +
                "            clientId: 'S2_fb903ce81792411ab6c459ec3a2a82c6',\n" +
                "            method: 'card',\n" +
                "            appScheme: `nicepaysample://`,\n" +
                "            orderId: '" + orderId + "',\n" +
                "            amount: " + amount + ",\n" +
                "            goodsName: '" + goodsName + "',\n" +
                "            returnUrl: 'http://13.209.4.64:8080/payment/verify',\n" +
                "            fnError: function (result) {\n" +
                "                alert('개발자확인용 : ' + result.errorMsg);\n" +
                "            }\n" +
                "        });\n" +
                "    }\n" +
                "</script>\n" +
                "\n" +
                "</body>\n" +
                "</html>";
    }

    @PostMapping("/verify")
    public ResponseEntity<OrderResultMessageDto> verifyPayment(
            @ModelAttribute PaymentVerifyRequestDto paymentVerifyResultDto,
            Model model) throws JsonProcessingException {

        model.addAttribute("paymentVerifyResultDto", paymentVerifyResultDto);

        String tid = paymentVerifyResultDto.getTid();

        HttpHeaders headers = new HttpHeaders();
        String clientKey = "S2_fb903ce81792411ab6c459ec3a2a82c6";
        String secretKey = "36e730803c7f45f3ae1462408083c94c";
        headers.set("Authorization", "Basic " + Base64.getEncoder()
                .encodeToString((clientKey + ":" + secretKey).getBytes(StandardCharsets.UTF_8)));
        headers.setContentType(MediaType.APPLICATION_JSON);

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

            JsonNode responseNode = response.getBody();
            String resultCode = responseNode.get("resultCode").asText();
            paymentVerifyResultDto.setAuthResultMsg(responseNode.get("resultMsg").asText());

            if ("0000".equalsIgnoreCase(resultCode)) {
                int tryCount = 5;

                while (tryCount > 0) {
                    try {
                        PaymentApproveResult result = mapper.treeToValue(responseNode, PaymentApproveResult.class);
                        nicePayService.MappingOrderWithPaymentApproveResult(result);
                        break;
                    } catch (IllegalArgumentException e) {
                        return ResponseEntity
                                .status(HttpStatus.MULTIPLE_CHOICES) // 300
                                .body(new OrderResultMessageDto(
                                        "[결제성공]IllegalArgumentException에러가 발생하였습니다.PaymentApproveResult와 order매핑에 실패하였습니다." + e.getMessage(),
                                        "MULTIPLE_CHOICES"));
                    } catch (Exception e) {
                        log.info("[info 에러 발생]", e.getMessage());
                        tryCount--;
                    }
                }

                if (tryCount == 0) {
                    return ResponseEntity
                            .status(HttpStatus.MULTIPLE_CHOICES) // 300
                            .body(new OrderResultMessageDto(
                                    "[결제성공]IllegalArgumentException를 제외한 에러가 발생하였습니다. 상위 에러메세지를 확인하세요.",
                                    "MULTIPLE_CHOICES"));
                }

                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(new OrderResultMessageDto("[결제성공] 결제에 성공했습니다.", "OK"));

            } else {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(new OrderResultMessageDto(
                                "[결제실패]Payment의 ResultCode가 0000이 아닙니다. 결제에 실패하였습니다.",
                                "BAD_REQUEST"));
            }

        } catch (HttpClientErrorException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new OrderResultMessageDto(
                            "[결제실패]클라이언트 에러 발생. " + e.getMessage(),
                            "BAD_REQUEST"));
        } catch (HttpServerErrorException e) {
            return ResponseEntity
                    .status(HttpStatus.MULTIPLE_CHOICES)
                    .body(new OrderResultMessageDto(
                            "[결제실패]서버에 에러 발생." + e.getMessage(),
                            "MULTIPLE_CHOICES"));
        }
    }

    @GetMapping(value = "/cancel")
    public ResponseEntity<OrderResultMessageDto> cancelDemo() {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new OrderResultMessageDto(
                        "[결제실패] 사용자가 결제를 취소했습니다.",
                        "UNPROCESSABLE_ENTITY"
                ));
    }}