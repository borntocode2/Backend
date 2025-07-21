package goodspace.backend.payment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestContoller {
    @GetMapping("/payment")
    public String showPaymentPage() {
        try {
            return "nicepay";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
