package hiyen.onboarding.user.presentation.response;

import hiyen.onboarding.user.domain.Authority.UserAuthority;


public record AuthorityResponse(

        String authorityName
) {
    public static AuthorityResponse of(UserAuthority userAuthority) {
        return new AuthorityResponse(userAuthority.name());
    }
}
