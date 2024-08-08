package hiyen.onboarding.user.presentation;

import hiyen.onboarding.global.auth.jwt.JwtProvider;
import hiyen.onboarding.user.application.TokenService;
import hiyen.onboarding.user.presentation.request.ReissueTokenRequest;
import hiyen.onboarding.user.presentation.response.TokenResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tokens")
public class TokenRestController {

    private final TokenService tokenService;

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestBody final ReissueTokenRequest request, final HttpServletResponse response) {

        final String accessToken = tokenService.reissue(request.refreshToken());
        final TokenResponse tokenResponse = new TokenResponse(accessToken);

        response.setHeader(JwtProvider.AUTHORIZATION_HEADER, JwtProvider.BEARER + accessToken);

        return ResponseEntity.ok()
                .body(tokenResponse);
    }
}
