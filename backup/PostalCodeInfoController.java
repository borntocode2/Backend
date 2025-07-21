package goodspace.backend.controller;

import goodspace.backend.dto.AddressDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PostalCodeInfoController {
    @PostMapping("/address")
    public ResponseEntity<String> registerAddress(@RequestBody AddressDto addressDto) {
        // TODO: 실제 저장 로직 작성 (DB 저장 등)

        // 저장 성공 응답
        return ResponseEntity.ok("주소 등록 성공");
    }
}
