package goodspace.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PostalCodeFindController {
    @GetMapping("/postalcode")
    public String postalcode() {
        return "postalCode";
    }
}
