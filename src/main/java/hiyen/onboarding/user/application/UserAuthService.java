package hiyen.onboarding.user.application;

import hiyen.onboarding.user.domain.Authority;
import hiyen.onboarding.user.domain.Authority.UserAuthority;
import hiyen.onboarding.user.domain.User;
import hiyen.onboarding.user.exception.UserException.FailLoginException;
import hiyen.onboarding.user.presentation.request.UserSignRequest;
import hiyen.onboarding.user.presentation.request.UserSignupRequest;
import hiyen.onboarding.user.repository.UserRepository;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User signup(final UserSignupRequest request) {

        final User user = User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .nickname(request.nickname())
                .authorities(Set.of(new Authority(UserAuthority.ROLE_USER)))
                .build();

        return userRepository.save(user);
    }

    public User sign(final UserSignRequest request) {

        final User found = userRepository.findByUsername(request.username())
                .orElseThrow(FailLoginException::new);

        if (!passwordEncoder.matches(request.password(), found.getPassword())) {
            throw new FailLoginException();
        }

        log.info("login success. User username : {}", found.getUsername());
        return found;
    }

}
