package goodspace.backend.authorization.controller;

import goodspace.backend.authorization.dto.naver.AppAuthRequestDto;
import goodspace.backend.authorization.dto.request.WebOauthRequestDto;
import goodspace.backend.authorization.dto.response.TokenResponseDto;
import goodspace.backend.authorization.service.apple.AppleOAuthService;
import goodspace.backend.authorization.service.facebook.FacebookOAuthService;
import goodspace.backend.authorization.service.google.GoogleOAuthService;
import goodspace.backend.authorization.service.kakao.KaKaoOAuthService;
import goodspace.backend.authorization.service.naver.NaverOAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.SEE_OTHER;

@RestController
@RequestMapping("/authorization")
@RequiredArgsConstructor
@Tag(
        name = "OAuth API",
        description = "소셜 회원가입, 로그인 관련 기능"
)
public class OAuthController {
    private final GoogleOAuthService googleOAuthService;
    private final KaKaoOAuthService kaKaoOAuthService;
    private final FacebookOAuthService facebookOAuthService;
    private final NaverOAuthService naverOAuthService;
    private final AppleOAuthService appleOAuthService;

    @PostMapping("/google/web")
    @Operation(
            summary = "구글 소셜 로그인(웹)",
            description = "구글을 통해 사용자를 인증하고 JWT를 발급합니다"
    )
    public ResponseEntity<TokenResponseDto> googleWebAuthorization(@RequestBody WebOauthRequestDto requestDto) {
        String accessToken = googleOAuthService.getAccessToken(requestDto.code());
        TokenResponseDto responseDto = googleOAuthService.signUpOrSignIn(accessToken);

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/google/app")
    @Operation(
            summary = "구글 소셜 로그인(앱)",
            description = "구글이 발급한 AccessToken을 통해 사용자를 인증하고 JWT를 발급합니다"
    )
    public ResponseEntity<TokenResponseDto> googleAppAuthorization(@RequestParam(name = "accessToken") String accessToken) {
        TokenResponseDto responseDto = googleOAuthService.signUpOrSignIn(accessToken);

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/google/redirection")
    @Operation(
            summary = "구글 로그인 화면 리다이렉트",
            description = "구글 로그인 화면으로 리다이렉트합니다"
    )
    public ResponseEntity<Void> googleRedirect() {
        return ResponseEntity.status(SEE_OTHER)
                .header(HttpHeaders.LOCATION, googleOAuthService.getOauthPageRedirectUrl())
                .build();
    }

    @PostMapping("/kakao/web")
    @Operation(
            summary = "카카오 소셜 로그인(웹)",
            description = "카카오가 발급한 code를 통해 사용자를 인증하고 JWT를 발급합니다."
    )
    public ResponseEntity<TokenResponseDto> kakaoWebAuthorization(@RequestBody WebOauthRequestDto requestDto) {
        String accessToken = kaKaoOAuthService.getAccessToken(requestDto.code());
        TokenResponseDto responseDto = kaKaoOAuthService.signUpOrSignIn(accessToken);

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/kakao/app")
    @Operation(
            summary = "카카오 소셜 로그인(앱)",
            description = "카카오가 발급한 AccessToken을 통해 사용자를 인증하고 JWT를 발급합니다."
    )
    public ResponseEntity<TokenResponseDto> kakaoAppAuthorization(@RequestParam(name = "accessToken") String accessToken) {
        TokenResponseDto responseDto = kaKaoOAuthService.signUpOrSignIn(accessToken);

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/kakao/redirection")
    @Operation(
            summary = "카카오 로그인 화면 리다이렉트",
            description = "카카오 로그인 화면으로 리다이렉트합니다"
    )
    public ResponseEntity<Void> kakaoRedirect() {
        return ResponseEntity.status(SEE_OTHER)
                .header(HttpHeaders.LOCATION, kaKaoOAuthService.getOauthPageRedirectUrl())
                .build();
    }

    @PostMapping("/facebook/web")
    @Operation(
            summary = "페이스북 소셜 로그인(웹)",
            description = "페이스북을 통해 사용자를 인증하고 JWT를 발급합니다"
    )
    public ResponseEntity<TokenResponseDto> facebookWebAuthorization(@RequestBody WebOauthRequestDto requestDto) {
        String accessToken = facebookOAuthService.getAccessToken(requestDto.code());
        TokenResponseDto responseDto = facebookOAuthService.signUpOrSignIn(accessToken);

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/facebook/app")
    @Operation(
            summary = "페이스북 소셜 로그인(앱)",
            description = "페이스북이 발급한 AccessToken 통해 사용자를 인증하고 JWT를 발급합니다"
    )
    public ResponseEntity<TokenResponseDto> facebookAppAuthorization(@RequestParam(name = "accessToken") String accessToken) {
        TokenResponseDto responseDto = facebookOAuthService.signUpOrSignIn(accessToken);

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/facebook/redirection")
    @Operation(
            summary = "페이스북 로그인 화면 리다이렉트",
            description = "페이스북 로그인 화면으로 리다이렉트합니다"
    )
    public ResponseEntity<Void> facebookRedirect() {
        return ResponseEntity.status(SEE_OTHER)
                .header(HttpHeaders.LOCATION, facebookOAuthService.getOauthPageRedirectUrl())
                .build();
    }

    @PostMapping("/naver/web")
    @Operation(
            summary = "네이버 소셜 로그인(웹)",
            description = "네이버를 통해 사용자를 인증하고 JWT를 발급합니다"
    )
    public ResponseEntity<TokenResponseDto> naverWebAuthorization(@RequestBody WebOauthRequestDto requestDto) {
        String accessToken = naverOAuthService.getAccessToken(requestDto.code());
        TokenResponseDto responseDto = naverOAuthService.signUpOrSignIn(accessToken);

        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/naver/app")
    @Operation(
            summary = "네이버 소셜 로그인(앱)",
            description = "네이버가 발급한 AccessToken을 통해 사용자를 인증하고 JWT를 발급합니다"
    )
    public ResponseEntity<TokenResponseDto> naverAppAuthorization(@RequestBody AppAuthRequestDto requestDto) {
        TokenResponseDto responseDto = naverOAuthService.signUpOrSignIn(requestDto.accessToken());

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/naver/redirection")
    @Operation(
            summary = "네이버 로그인 화면 리다이렉트",
            description = "네이버 로그인 화면으로 리다이렉트합니다"
    )
    public ResponseEntity<Void> naverRedirect() {
        return ResponseEntity.status(SEE_OTHER)
                .header(HttpHeaders.LOCATION, naverOAuthService.getOauthPageRedirectUrl())
                .build();
    }

    @PostMapping("/apple/web")
    @Operation(
            summary = "애플 소셜 로그인(웹)",
            description = "애플을 통해 사용자를 인증하고 JWT를 발급합니다"
    )
    public ResponseEntity<TokenResponseDto> appleWebAuthorization(@RequestBody WebOauthRequestDto requestDto) {
        String accessToken = appleOAuthService.getAccessToken(requestDto.code());
        TokenResponseDto responseDto = appleOAuthService.signUpOrSignIn(accessToken);

        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/apple/app")
    @Operation(
            summary = "애플 소셜 로그인(앱)",
            description = "애플이 발급한 Apple ID Token을 통해 사용자를 인증하고 JWT를 발급합니다"
    )
    public ResponseEntity<TokenResponseDto> appleAppAuthorization(@RequestBody AppAuthRequestDto requestDto) {
        TokenResponseDto responseDto = appleOAuthService.signUpOrSignIn(requestDto.accessToken());

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/apple/redirection")
    @Operation(
            summary = "애플 로그인 화면 리다이렉트",
            description = "애플 로그인 화면으로 리다이렉트합니다"
    )
    public ResponseEntity<Void> appleRedirect() {
        return ResponseEntity.status(SEE_OTHER)
                .header(HttpHeaders.LOCATION, appleOAuthService.getOauthPageRedirectUrl())
                .build();
    }
}
