package hiyen.onboarding.user.domain;

import hiyen.onboarding.global.auth.AuthDTO;

public record RefreshToken(

        String refreshToken,
        AuthDTO authDTO
) {

}
