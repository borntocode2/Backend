package goodspace.backend.client.controller;

import goodspace.backend.client.dto.ClientBriefInfoResponseDto;
import goodspace.backend.client.dto.ClientDetailsResponseDto;
import goodspace.backend.client.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/client")
@Tag(
        name = "클라이언트 API",
        description = "클라이언트 정보 및 굿즈 정보 관련 기능"
)
public class ClientController {
    private final ClientService clientService;

    @GetMapping
    @Operation(
            summary = "클라이언트 목록 조회",
            description = "모든 클라이언트에 대한 간략한 정보를 반환합니다"
    )
    public ResponseEntity<List<ClientBriefInfoResponseDto>> findClients() {
        List<ClientBriefInfoResponseDto> responseDto = clientService.getClients();

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{clientId}")
    @Operation(
            summary = "클라이언트 조회",
            description = "식별자와 일치하는 클라이언트의 정보를 반환합니다(랜딩 페이지 용도)"
    )
    public ResponseEntity<ClientDetailsResponseDto> findClientDetails(@PathVariable long clientId) {
        ClientDetailsResponseDto responseDto = clientService.getDetails(clientId);

        return ResponseEntity.ok(responseDto);
    }
}
