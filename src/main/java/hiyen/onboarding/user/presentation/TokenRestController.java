package hiyen.onboarding.user.presentation;

import hiyen.onboarding.global.auth.jwt.JwtProvider;
import hiyen.onboarding.user.application.TokenService;
import hiyen.onboarding.user.presentation.request.ReissueTokenRequest;
import hiyen.onboarding.user.presentation.response.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "TokenRestController", description = "토큰 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tokens")
public class TokenRestController {

    private final TokenService tokenService;

    @PostMapping("/refresh")
    @Operation(summary = "토큰 재발급", description = "리프레시 토큰을 이용하여 엑세스 토큰을 재발급합니다.")
    public ResponseEntity<TokenResponse> refresh(@RequestBody final ReissueTokenRequest request, final HttpServletResponse response) {

        final String accessToken = tokenService.reissue(request.refreshToken());
        final TokenResponse tokenResponse = new TokenResponse(accessToken);

        response.setHeader(JwtProvider.AUTHORIZATION_HEADER, JwtProvider.BEARER + accessToken);

        return ResponseEntity.ok()
                .body(tokenResponse);
    }
}
