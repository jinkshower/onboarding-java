package hiyen.onboarding.global.auth;

import hiyen.onboarding.user.domain.Authority;
import hiyen.onboarding.user.domain.User;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final AuthDTO authDTO;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Authority> authorities = authDTO.authorities();

        return authorities.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority().name()))
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return authDTO.username();
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
