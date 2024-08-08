package hiyen.onboarding.user.application;

import hiyen.onboarding.global.auth.AuthDTO;
import hiyen.onboarding.global.auth.AuthException.FailAuthenticationUserException;
import hiyen.onboarding.global.auth.jwt.JwtProvider;
import hiyen.onboarding.user.domain.RefreshToken;
import hiyen.onboarding.user.domain.User;
import hiyen.onboarding.user.repository.RefreshTokenRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public String createAccessToken(final User login) {
        return jwtProvider.createToken(login.getUsername(), login.getAuthorities());
    }

    public String createRefreshToken(final User login) {
        final RefreshToken refreshToken = new RefreshToken(
                UUID.randomUUID().toString(), new AuthDTO(login.getUsername(), login.getAuthorities()));
        refreshTokenRepository.save(refreshToken);

        return refreshToken.refreshToken();
    }

    public String reissue(final String refreshToken) {
        final AuthDTO authDTO = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(FailAuthenticationUserException::new);

        return jwtProvider.createToken(authDTO.username(), authDTO.authorities());
    }
}
