package goodspace.backend.admin.controller;

import goodspace.backend.admin.dto.client.ClientInfoResponseDto;
import goodspace.backend.admin.dto.client.ClientRegisterRequestDto;
import goodspace.backend.admin.dto.client.ClientUpdateRequestDto;
import goodspace.backend.admin.service.client.ClientManageService;
import goodspace.backend.client.domain.ClientType;
import goodspace.backend.client.domain.RegisterStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/admin/client")
@RequiredArgsConstructor
@Tag(
        name = "클라이언트 관리 API",
        description = "클라이언트 관리 관련 기능(관리자 전용)"
)
public class ClientManageController {
    private final ClientManageService clientManageService;

    @GetMapping
    @Operation(
            summary = "클라이언트 목록 조회",
            description = "모든 클라이언트를 조회합니다."
    )
    public ResponseEntity<List<ClientInfoResponseDto>> getClients() {
        List<ClientInfoResponseDto> response = clientManageService.findAll();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{clientId}")
    @Operation(
            summary = "클라이언트 단건 조회",
            description = "특정 클라이언트의 정보를 조회합니다."
    )
    public ResponseEntity<ClientInfoResponseDto> findById(@PathVariable Long clientId) {
        ClientInfoResponseDto response = clientManageService.findById(clientId);

        return ResponseEntity.ok(response);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "클라이언트 생성",
            description = "새로운 클라이언트를 생성합니다."
    )
    public ResponseEntity<Void> createClient(
            @RequestPart("name") String name,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
            @RequestPart(value = "backgroundImage", required = false) MultipartFile backgroundImage,
            @RequestPart("introduction") String introduction,
            @RequestPart("clientType") ClientType clientType
    ) {
        clientManageService.register(ClientRegisterRequestDto.builder()
                .name(name)
                .profileImage(profileImage)
                .backgroundImage(backgroundImage)
                .introduction(introduction)
                .clientType(clientType)
                .build());

        return ResponseEntity.ok().build();
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "클라이언트 수정",
            description = "클라이언트의 정보를 수정합니다."
    )
    public ResponseEntity<Void> updateClient(
            @RequestPart("id") Long id,
            @RequestPart("name") String name,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
            @RequestPart(value = "backgroundImage", required = false) MultipartFile backgroundImage,
            @RequestPart("introduction") String introduction,
            @RequestPart("clientType") ClientType clientType,
            @RequestPart("status") RegisterStatus status
    ) {
        clientManageService.update(ClientUpdateRequestDto.builder()
                .id(id)
                .name(name)
                .profileImage(profileImage)
                .backgroundImage(backgroundImage)
                .introduction(introduction)
                .clientType(clientType)
                .status(status)
                .build());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{clientId}")
    @Operation(
            summary = "클라이언트 제거",
            description = "클라이언트를 제거합니다."
    )
    public ResponseEntity<Void> deleteClient(@PathVariable Long clientId) {
        clientManageService.delete(clientId);

        return ResponseEntity.ok().build();
    }
}
