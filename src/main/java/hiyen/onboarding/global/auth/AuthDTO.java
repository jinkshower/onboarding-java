package hiyen.onboarding.global.auth;

import hiyen.onboarding.user.domain.Authority;
import java.util.Set;

public record AuthDTO(

        String username,
        Set<Authority> authorities
) {
}
