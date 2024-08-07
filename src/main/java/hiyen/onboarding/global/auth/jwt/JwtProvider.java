package hiyen.onboarding.global.auth.jwt;

import hiyen.onboarding.user.domain.Authority;
import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
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

    public String getAuthority(final String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload()
                .get(AUTHORITY, String.class);
    }

    public Boolean isExpired(final String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration()
                .before(new Date());
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

}
