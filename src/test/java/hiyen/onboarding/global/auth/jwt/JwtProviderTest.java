package hiyen.onboarding.global.auth.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import hiyen.onboarding.user.domain.Authority;
import hiyen.onboarding.user.domain.Authority.UserAuthority;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.AuthenticationException;

class JwtProviderTest {

    private static final String JWT_SECRET_KEY = "A".repeat(32); // Secret Key는 최소 32바이트 이상이어야함.
    private static final Long TOKEN_EXPIRATION = 3600L;
    private static final String USER_NAME = "testUser";
    private static final Set<Authority> AUTHORITIES = Set.of(new Authority(UserAuthority.ROLE_USER));

    private final JwtProvider jwtTokenProvider = new JwtProvider(JWT_SECRET_KEY,
            TOKEN_EXPIRATION);

    @DisplayName("엑세스 토큰을 생성한다.")
    @Test
    void success_create() {
        // given & when
        String actual = jwtTokenProvider.createToken(USER_NAME, AUTHORITIES);

        // then
        assertThat(actual.split("\\.")).hasSize(3);
    }

    @DisplayName("토큰의 username을 가져온다.")
    @Test
    void success_getUsername() {
        // given
        String token = jwtTokenProvider.createToken(USER_NAME, AUTHORITIES);

        // when
        String actual = jwtTokenProvider.getUsername(token);

        // then
        assertThat(actual).isEqualTo(USER_NAME);
    }

    @DisplayName("토큰의 authorities를 가져온다.")
    @Test
    void success_getAuthorities() {
        // given
        String token = jwtTokenProvider.createToken(USER_NAME, AUTHORITIES);

        // when
        Set<Authority> actual = jwtTokenProvider.getAuthority(token);

        // then
        assertThat(actual).isEqualTo(AUTHORITIES);
    }


    @DisplayName("토큰이 만료된 경우 예외를 던진다.")
    @Test
    void fail_expiredToken() {
        // given
        JwtProvider expiredJwtProvider = new JwtProvider(JWT_SECRET_KEY, 0L);
        String expiredToken = expiredJwtProvider.createToken(USER_NAME, AUTHORITIES);

        // when & then
        assertThatThrownBy(() -> expiredJwtProvider.validateToken(expiredToken))
                .isInstanceOf(AuthenticationException.class);
    }

    @DisplayName("토큰이 유효하지 않으면 예외를 던진다.")
    @Test
    void fail_malformedToken() {
        // given
        String malformedToken = "malformed";

        // when & then
        assertThatThrownBy(() -> jwtTokenProvider.validateToken(malformedToken))
                .isInstanceOf(AuthenticationException.class);
    }
}
