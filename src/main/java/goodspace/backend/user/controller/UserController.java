package goodspace.backend.user.controller;

import goodspace.backend.global.principal.PrincipalUtil;
import goodspace.backend.user.dto.*;
import goodspace.backend.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(
        name = "회원 API",
        description = "회원 정보 관련 기능"
)
public class UserController {
    private final UserService userService;
    private final PrincipalUtil principalUtil;

    @GetMapping("/getInfo")
    @Operation(
            summary = "정보 불러오기",
            description = "회원 정보를 불러옵니다."
    )
    public ResponseEntity<UserMyPageResponseDto> getUserInfo(Principal principal) {
        long id = principalUtil.findIdFromPrincipal(principal);
        return ResponseEntity.ok().body(userService.getUserInfo(id));
    }

    @GetMapping("/name")
    @Operation(
            summary = "이름 불러오기",
            description = "회원 이름을 조회합니다."
    )
    public ResponseEntity<UserNameResponseDto> getUserName(Principal principal) {
        long id = principalUtil.findIdFromPrincipal(principal);
        UserNameResponseDto responseDto = userService.getName(id);

        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/updateMyPage")
    @Operation(
            summary = "정보 수정",
            description = "회원 정보를 수정합니다.(마이페이지 용도)"
    )
    public ResponseEntity<String> updateMyPage(
            Principal principal,
            @RequestBody UserMyPageDto userMyPageDto
    ){
        long id = principalUtil.findIdFromPrincipal(principal);
        return ResponseEntity.ok().body(userService.updateMyPage(id, userMyPageDto));
    }

    @PatchMapping("/password")
    @Operation(
            summary = "비밀번호 수정(마이페이지 용도)",
            description = "비밀번호를 수정합니다."
    )
    public ResponseEntity<RefreshTokenResponseDto> updatePassword(
            Principal principal,
            @RequestBody PasswordUpdateRequestDto requestDto
    ) {
        long id = principalUtil.findIdFromPrincipal(principal);
        RefreshTokenResponseDto responseDto = userService.updatePassword(id, requestDto);

        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/forget-password")
    @Operation(
            summary = "비밀번호 수정(비밀번호를 잊어버렸을 때)",
            description = "비밀번호를 수정합니다. 사전에 이메일 인증이 필요한 대신 AccessToken을 필요로 하지 않습니다."
    )
    public ResponseEntity<RefreshTokenResponseDto> updatePasswordByVerifiedEmail(
            @RequestBody PasswordUpdateByVerifiedEmailRequestDto requestDto
    ) {
        RefreshTokenResponseDto responseDto = userService.updatePasswordByVerifiedEmail(requestDto);

        return ResponseEntity.ok(responseDto);
    }

    @PatchMapping("/email")
    @Operation(
            summary = "이메일 수정",
            description = "이메일을 수정합니다. (사전에 이메일 인증 필요)"
    )
    public ResponseEntity<RefreshTokenResponseDto> updateEmail(
            Principal principal,
            @RequestBody EmailUpdateRequestDto requestDto
    ) {
        long id = principalUtil.findIdFromPrincipal(principal);
        RefreshTokenResponseDto responseDto = userService.updateEmail(id, requestDto);

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/purchase-history")
    @Operation(
            summary = "결재 내역 확인",
            description = "회원의 결재 내역 목록을 조회합니다."
    )
    public ResponseEntity<List<PurchaseHistoryResponseDto>> getPurchaseHistory(Principal principal) {
        long id = principalUtil.findIdFromPrincipal(principal);
        List<PurchaseHistoryResponseDto> responseDtos = userService.getPurchaseHistory(id);

        return ResponseEntity.ok(responseDtos);
    }

    @DeleteMapping
    @Operation(
            summary = "회원 탈퇴",
            description = "회원을 제거합니다."
    )
    public ResponseEntity<Void> withdraw(Principal principal) {
        long userId = principalUtil.findIdFromPrincipal(principal);
        userService.removeUser(userId);

        return ResponseEntity.noContent().build();
    }
}
