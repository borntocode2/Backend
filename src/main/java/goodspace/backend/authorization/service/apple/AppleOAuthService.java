package goodspace.backend.authorization.service.apple;

import com.google.gson.Gson;
import goodspace.backend.authorization.dto.apple.AppleIdTokenPayload;
import goodspace.backend.authorization.dto.apple.AppleTokenResponse;
import goodspace.backend.authorization.dto.response.TokenResponseDto;
import goodspace.backend.authorization.service.OAuthService;
import goodspace.backend.global.security.Role;
import goodspace.backend.global.security.TokenProvider;
import goodspace.backend.global.security.TokenType;
import goodspace.backend.user.domain.OAuthType;
import goodspace.backend.user.domain.OAuthUser;
import goodspace.backend.user.domain.User;
import goodspace.backend.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.Signature;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.function.Supplier;

@Service
@Slf4j
public class AppleOAuthService implements OAuthService {

    private static final String TOKEN_URL = "https://appleid.apple.com/auth/token";
    private static final String AUTHORIZE_URL = "https://appleid.apple.com/auth/authorize";
    private static final String AUDIENCE = "https://appleid.apple.com";
    private static final String SCOPE = "name email"; // 권장 스코프

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    private final String clientId;     // Service ID 또는 App ID (com.example.app)
    private final String teamId;       // Apple Developer Team ID
    private final String keyId;        // Apple Key ID
    private final String privateKeyPem; // .p8 PEM 문자열 (PKCS#8)
    private final String redirectUri;

    public AppleOAuthService(
            UserRepository userRepository,
            TokenProvider tokenProvider,
            @Value("${keys.apple.client-id}") String clientId,
            @Value("${keys.apple.team-id}") String teamId,
            @Value("${keys.apple.key-id}") String keyId,
            @Value("${keys.apple.private-key}") String privateKeyPem,
            @Value("${keys.apple.redirect-uri}") String redirectUri
    ) {
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
        this.clientId = clientId;
        this.teamId = teamId;
        this.keyId = keyId;
        this.privateKeyPem = privateKeyPem;
        this.redirectUri = redirectUri;
    }

    @Override
    public String getAccessToken(String code) {
        String clientSecret = createClientSecret();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("code", code);
        params.add("redirect_uri", redirectUri);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = new RestTemplate()
                .exchange(TOKEN_URL, HttpMethod.POST, entity, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            log.warn("애플 토큰 교환 실패. status={} body={}", response.getStatusCode(), response.getBody());
            throw new RuntimeException("애플 토큰 교환 실패");
        }

        AppleTokenResponse token = new Gson().fromJson(response.getBody(), AppleTokenResponse.class);

        if (token == null || token.getIdToken() == null) {
            log.warn("애플 토큰 응답 파싱 실패. body={}", response.getBody());
            throw new RuntimeException("애플 토큰 응답 파싱 실패");
        }

        return token.getIdToken();
    }

    @Override
    @Transactional
    public TokenResponseDto signUpOrSignIn(String appleIdToken) {
        AppleIdTokenPayload payload = parseIdTokenPayload(appleIdToken);

        if (payload.getEmail() != null && payload.getEmailVerified() != null && !payload.getEmailVerified()) {
            throw new IllegalStateException("Apple 이메일이 검증되지 않았습니다.");
        }

        String appleSub = payload.getSub();
        User user = userRepository.findByIdentifierAndOAuthType(appleSub, OAuthType.APPLE)
                .orElseGet(saveNewUser(payload));

        String accessTokenValue = tokenProvider.createToken(user.getId(), TokenType.ACCESS, user.getRoles());
        String refreshTokenValue = tokenProvider.createToken(user.getId(), TokenType.REFRESH, user.getRoles());
        user.updateRefreshToken(refreshTokenValue);

        return new TokenResponseDto(accessTokenValue, refreshTokenValue);
    }

    @Override
    public String getOauthPageRedirectUrl() {
        return AUTHORIZE_URL +
                "?client_id=" + urlEncode(clientId) +
                "&redirect_uri=" + urlEncode(redirectUri) +
                "&response_type=code" +
                "&response_mode=form_post" +
                "&scope=" + urlEncode(SCOPE);
    }

    private String createClientSecret() {
        try {
            long now = Instant.now().getEpochSecond();
            long exp = now + 60L * 60L * 24L * 180L; // 최대 6개월

            String headerJson = new Gson().toJson(Map.of(
                    "alg", "ES256",
                    "kid", keyId,
                    "typ", "JWT"
            ));

            String payloadJson = new Gson().toJson(Map.of(
                    "iss", teamId,
                    "iat", now,
                    "exp", exp,
                    "aud", AUDIENCE,
                    "sub", clientId
            ));

            String headerB64 = base64UrlEncode(headerJson.getBytes(StandardCharsets.UTF_8));
            String payloadB64 = base64UrlEncode(payloadJson.getBytes(StandardCharsets.UTF_8));
            String signingInput = headerB64 + "." + payloadB64;

            byte[] signatureDer = signEs256(signingInput.getBytes(StandardCharsets.UTF_8), loadECPrivateKeyFromPem(privateKeyPem));
            String signatureB64 = base64UrlEncode(signatureDer);

            return signingInput + "." + signatureB64;
        } catch (Exception e) {
            log.error("Apple client_secret 생성 실패", e);
            throw new RuntimeException("Apple client_secret 생성 실패", e);
        }
    }

    private AppleIdTokenPayload parseIdTokenPayload(String idToken) {
        try {
            String[] parts = idToken.split("\\.");
            if (parts.length < 2) throw new IllegalArgumentException("잘못된 id_token 형식");

            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
            return new Gson().fromJson(payloadJson, AppleIdTokenPayload.class);
        } catch (Exception e) {
            log.error("Apple id_token 파싱 실패", e);
            throw new RuntimeException("Apple id_token 파싱 실패", e);
        }
    }

    private static byte[] signEs256(byte[] data, ECPrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withECDSA");
        signature.initSign(privateKey);
        signature.update(data);

        return signature.sign();
    }

    private static ECPrivateKey loadECPrivateKeyFromPem(String pem) throws Exception {
        String sanitized = pem
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");
        byte[] pkcs8 = Base64.getDecoder().decode(sanitized);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pkcs8);
        return (ECPrivateKey) KeyFactory.getInstance("EC").generatePrivate(keySpec);
    }

    private static String base64UrlEncode(byte[] bytes) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private static String urlEncode(String s) {
        return java.net.URLEncoder.encode(s, StandardCharsets.UTF_8);
    }

    private Supplier<OAuthUser> saveNewUser(AppleIdTokenPayload payload) {
        return () -> {
            OAuthUser newUser = userRepository.save(payload.toEntity());
            newUser.addRole(Role.USER);

            return newUser;
        };
    }
}
