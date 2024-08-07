package hiyen.onboarding.user.presentation.response;

import hiyen.onboarding.user.domain.Authority;
import java.util.Set;

public record UserResponse(

        String username,
        String nickname,
        Set<AuthorityResponse> authorities
){
}
