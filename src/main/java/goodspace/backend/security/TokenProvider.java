package goodspace.backend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.security.Principal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TokenProvider {

    private static final String TOKEN_TYPE_CLAIM = "PlanFit/TokenType";
    private static final String BEARER = "Bearer ";
    private static final String AUTHORIZATION = "Authorization";
    private static final String ROLES = "roles";

    private final long validityTime;
    private final Key key;

    public TokenProvider(
            @Value("${keys.jwt.secret}") String jwtSecret,
            @Value("${keys.jwt.access-token-validity-in-milliseconds}") long validityTime
    ) {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.validityTime = validityTime;
    }

    public String createToken(long id, TokenType tokenType, List<Role> roles) {
        // 토큰의 타입에 맞게 만료 시간을 지정함(리프레쉬 토큰의 지속시간을 24배 길게 설정함)
        Date accessTokenExpiredTime;
        switch (tokenType) {
            case ACCESS -> {
                accessTokenExpiredTime = new Date(new Date().getTime() + validityTime);
            }
            case REFRESH -> {
                accessTokenExpiredTime = new Date(new Date().getTime() + (validityTime * 24));
            }
            default ->  {
                throw new IllegalArgumentException("토큰 생성 실패: 부적절한 TokenType 입니다.");
            }
        }

        return Jwts.builder()
                .setSubject(Long.toString(id))
                .claim(TOKEN_TYPE_CLAIM, tokenType.name())
                .claim(ROLES, convertRolesToString(roles))
                .setIssuedAt(new Date())
                .setExpiration(accessTokenExpiredTime)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);

        String rolesString = claims.get(ROLES, String.class);
        List<SimpleGrantedAuthority> authorities = Arrays.stream(rolesString.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(claims.getSubject(), null, authorities);
        authentication.setDetails(claims);

        return authentication;
    }

    /**
     * 토큰 앞의 "Bearer "를 제거하는 메서드
     */
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER)) {
            return bearerToken.substring(7);
        }

        // 토큰이 Bearer 형식이 아닐 경우, 부적절한 토큰이므로 null 을 반환함
        return null;
    }

    /**
     * 토큰의 유효성을 검증하는 메서드
     */
    public boolean validateToken(String token, TokenType tokenType) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return hasProperType(token, tokenType);
        } catch (UnsupportedJwtException | ExpiredJwtException | IllegalArgumentException | MalformedJwtException e) {
            return false;
        }
    }

    /**
     * 문자열 토큰을 Claims 로 변환하는 메서드
     */
    public Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        } catch (SignatureException e) {
            throw new IllegalArgumentException("토큰 복호화 실패: 부적절한 토큰입니다.");
        }
    }

    public long getIdFromToken(String token) {
        Claims claims = parseClaims(token);

        String subject = claims.getSubject();
        if (!StringUtils.hasText(subject)) {
            throw new IllegalArgumentException("토큰에 사용자 ID 정보가 없습니다.");
        }

        try {
            return Long.parseLong(subject);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("토큰의 subject 값을 Long으로 변환할 수 없습니다.", e);
        }
    }

    /**
     * 토큰의 타입이 파라미터로 넘긴 타입과 일치하는지를 반환하는 메서드
     */
    private boolean hasProperType(String token, TokenType tokenType) {
        Claims claims = parseClaims(token);
        String tokenTypeClaim = (String) claims.get(TOKEN_TYPE_CLAIM);

        return tokenType == TokenType.valueOf(tokenTypeClaim);
    }

    private String convertRolesToString(List<Role> roles) {
        return roles.stream()
                .map(Role::toString)
                .collect(Collectors.joining(","));
    }

    public static Long getUserIdFromPrincipal(Principal principal) {
        return Long.parseLong(principal.getName());
    }
}
