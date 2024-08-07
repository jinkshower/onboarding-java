package hiyen.onboarding.user.application;

import hiyen.onboarding.user.domain.Authority;
import hiyen.onboarding.user.domain.Authority.UserAuthority;
import hiyen.onboarding.user.domain.User;
import hiyen.onboarding.user.presentation.request.UserSignupRequest;
import hiyen.onboarding.user.repository.UserRepository;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAuthService {

    private final UserRepository userRepository;

    public User signup(final UserSignupRequest request) {

        final User user = User.builder()
                .username(request.username())
                .password(request.password())
                .nickname(request.nickname())
                .authorities(Set.of(new Authority(UserAuthority.ROLE_USER)))
                .build();

        return userRepository.save(user);
    }
}
