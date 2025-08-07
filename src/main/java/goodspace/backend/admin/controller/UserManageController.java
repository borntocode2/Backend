package goodspace.backend.admin.controller;

import goodspace.backend.admin.dto.user.UserInfoDto;
import goodspace.backend.admin.service.user.UserManageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/user")
@RequiredArgsConstructor
@Tag(
        name = "회원 API(관리자 전용)",
        description = "회원 정보 조회/수정 관련 기능"
)
public class UserManageController {
    private final UserManageService userManageService;

    @GetMapping
    @Operation(
            summary = "회원 조회",
            description = "모든 회원을 조회합니다."
    )
    public ResponseEntity<List<UserInfoDto>> getUsers() {
        List<UserInfoDto> response = userManageService.getUsers();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    @Operation(
            summary = "회원 삭제",
            description = "특정 회원을 제거합니다."
    )
    public ResponseEntity<Void> deleteUser(@RequestParam Long userId) {
        userManageService.removeUser(userId);

        return ResponseEntity.noContent().build();
    }
}
