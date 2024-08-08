package hiyen.onboarding.user.presentation;

import hiyen.onboarding.global.auth.jwt.JwtProvider;
import hiyen.onboarding.user.application.TokenService;
import hiyen.onboarding.user.application.UserService;
import hiyen.onboarding.user.domain.Authority;
import hiyen.onboarding.user.domain.User;
import hiyen.onboarding.user.presentation.request.UserSignRequest;
import hiyen.onboarding.user.presentation.request.UserSignupRequest;
import hiyen.onboarding.user.presentation.response.AuthorityResponse;
import hiyen.onboarding.user.presentation.response.TokenResponse;
import hiyen.onboarding.user.presentation.response.UserResponse;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;
    private final TokenService tokenService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(@RequestBody @Validated final UserSignupRequest request) {

        final User created = userService.signup(request);
        final UserResponse response = new UserResponse(
                created.getUsername(), created.getNickname(), toAuthorityResponse(created.getAuthorities()));
        final URI uri = URI.create("/api/users/" + created.getUserId());

        return ResponseEntity.created(uri)
                .body(response);
    }

    @PostMapping("/sign")
    public ResponseEntity<TokenResponse> sign(@RequestBody final UserSignRequest request, final HttpServletResponse response) {

        final User login = userService.sign(request);
        final String accessToken = tokenService.createAccessToken(login);
        final String refreshToken = tokenService.createRefreshToken(login);
        final TokenResponse tokenResponse = new TokenResponse(accessToken);

        response.setHeader(JwtProvider.AUTHORIZATION_HEADER, JwtProvider.BEARER + accessToken);
        response.setHeader("Refresh-Token", refreshToken);

        return ResponseEntity.ok()
                .body(tokenResponse);
    }

    @GetMapping("/check")
    public ResponseEntity<String> check(Authentication authentication) {
        return ResponseEntity.ok(authentication.getName());
    }

    private Set<AuthorityResponse> toAuthorityResponse(final Set<Authority> authorities) {
        return authorities.stream()
                .map(authority -> AuthorityResponse.of(authority.getAuthority()))
                .collect(Collectors.toSet());
    }
}
