package hiyen.onboarding.global.util;

import hiyen.onboarding.user.domain.Authority;
import hiyen.onboarding.user.domain.Authority.UserAuthority;
import hiyen.onboarding.user.repository.AuthorityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final AuthorityRepository authorityRepository;

    @Override
    public void run(String... args) {
        authorityRepository.save(new Authority(UserAuthority.ROLE_USER));
        authorityRepository.save(new Authority(UserAuthority.ROLE_ADMIN));
    }
}
