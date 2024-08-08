package hiyen.onboarding.user.repository;

import hiyen.onboarding.global.auth.AuthDTO;
import hiyen.onboarding.global.auth.AuthException.FailAuthenticationUserException;
import hiyen.onboarding.user.domain.RefreshToken;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {

    private static final int REFRESH_TOKEN_EXPIRATION = 60;

    private final RedisTemplate<String, Object> redisTemplate;

    public void save(final RefreshToken refreshToken) {
        redisTemplate.opsForValue().set(
                refreshToken.refreshToken(), refreshToken.authDTO(), REFRESH_TOKEN_EXPIRATION, TimeUnit.MINUTES);
    }

    public Optional<AuthDTO> findByRefreshToken(final String refreshToken) {
        return Optional.ofNullable((AuthDTO) redisTemplate.opsForValue().get(refreshToken));
    }
}
