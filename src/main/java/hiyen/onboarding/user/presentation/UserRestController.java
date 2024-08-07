package hiyen.onboarding.user.presentation;

import hiyen.onboarding.user.application.UserAuthService;
import hiyen.onboarding.user.domain.Authority;
import hiyen.onboarding.user.domain.User;
import hiyen.onboarding.user.presentation.request.UserSignupRequest;
import hiyen.onboarding.user.presentation.response.AuthorityResponse;
import hiyen.onboarding.user.presentation.response.UserResponse;
import java.net.URI;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserRestController {

    private final UserAuthService userAuthService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signup(@RequestBody @Validated final UserSignupRequest request) {

        final User created = userAuthService.signup(request);
        final UserResponse response = new UserResponse(
                created.getUsername(), created.getNickname(), toAuthorityResponse(created.getAuthorities()));
        final URI uri = URI.create("/api/users/" + created.getUserId());

        return ResponseEntity.created(uri)
                .body(response);
    }

    private Set<AuthorityResponse> toAuthorityResponse(final Set<Authority> authorities) {
        return authorities.stream()
                .map(authority -> AuthorityResponse.of(authority.getAuthority()))
                .collect(Collectors.toSet());
    }
}
