package goodspace.backend.user.controller;

import goodspace.backend.user.dto.*;
import goodspace.backend.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static java.lang.Long.parseLong;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(
        name = "회원 API",
        description = "회원 정보 관련 기능"
)
public class UserController {
    private final UserService userService;

    @GetMapping("/getInfo")
    @Operation(
            summary = "정보 불러오기",
            description = "회원 정보를 불러옵니다."
    )
    public ResponseEntity<UserMyPageResponseDto> getUserInfo(Principal principal) {
        long id = parseLong(principal.getName());
        return ResponseEntity.ok().body(userService.getUserInfo(id));
    }


    @PatchMapping("/updateMyPage")
    @Operation(
            summary = "정보 수정",
            description = "회원 정보를 수정합니다.(마이페이지 용도)"
    )
    public ResponseEntity<String> updateMyPage(Principal principal, @RequestBody UserMyPageDto userMyPageDto){
        long id = parseLong(principal.getName());
        return ResponseEntity.ok().body(userService.updateMyPage(id, userMyPageDto));
    }

    @PatchMapping("/password")
    @Operation(
            summary = "비밀번호 수정",
            description = "비밀번호를 수정합니다."
    )
    public ResponseEntity<RefreshTokenResponseDto> updatePassword(Principal principal, @RequestBody PasswordUpdateRequestDto requestDto) {
        long id = parseLong(principal.getName());
        RefreshTokenResponseDto responseDto = userService.updatePassword(id, requestDto);

        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/email")
    @Operation(
            summary = "이메일 수정",
            description = "이메일을 수정합니다. (사전에 이메일 인증 필요)"
    )
    public ResponseEntity<RefreshTokenResponseDto> updateEmail(Principal principal, @RequestBody EmailUpdateRequestDto requestDto) {
        long id = parseLong(principal.getName());
        RefreshTokenResponseDto responseDto = userService.updateEmail(id, requestDto);

        return ResponseEntity.ok(responseDto);
    }
}
