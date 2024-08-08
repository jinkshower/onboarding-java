package hiyen.onboarding.global.auth.jwt;

import hiyen.onboarding.user.domain.Authority;
import hiyen.onboarding.user.domain.Authority.UserAuthority;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER = "Bearer ";
    public static final String AUTHORITY_DELIMITER = ",";

    private static final String USERNAME = "username";
    private static final String AUTHORITY = "authority";

    private final SecretKey secretKey;
    private final Long expiration;

    public JwtProvider(@Value("${spring.jwt.secret}") final String jwtSecret,
            @Value("${spring.jwt.expiration}") final Long expiration) {
        this.secretKey = new SecretKeySpec(jwtSecret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm());
        this.expiration = expiration;
    }

    public String getUsername(final String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
                .get(USERNAME, String.class);
    }

    public Set<Authority> getAuthority(final String token) {

        return toAuthorities(Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
                .get(AUTHORITY, String.class));
    }

    public void validateToken(final String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);

            claims.getPayload()
                    .getExpiration()
                    .before(new Date());
        } catch (final JwtException | IllegalArgumentException e) {
            throw new AuthenticationServiceException(e.getMessage(), e);
        }
    }

    public String createToken(final String username, final Set<Authority> authorities) {

        final String authority = authorities.stream()
                .map(it -> it.getAuthority().name())
                .collect(Collectors.joining(AUTHORITY_DELIMITER));

        return Jwts.builder()
                .claim(USERNAME, username)
                .claim(AUTHORITY, authority)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secretKey).compact();
    }

    private Set<Authority> toAuthorities(final String tokenAuthority) {
        String[] split = tokenAuthority.split(JwtProvider.AUTHORITY_DELIMITER);
        return Arrays.stream(split)
                .map(UserAuthority::of)
                .map(Authority::new)
                .collect(Collectors.toSet());
    }
}
