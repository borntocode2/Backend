package goodspace.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
}

