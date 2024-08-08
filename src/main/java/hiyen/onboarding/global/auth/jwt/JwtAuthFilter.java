package hiyen.onboarding.global.auth.jwt;

import hiyen.onboarding.global.auth.AuthDTO;
import hiyen.onboarding.global.auth.CustomUserDetails;
import hiyen.onboarding.user.domain.Authority;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader(JwtProvider.AUTHORIZATION_HEADER);

        if (authorizationHeader == null || !authorizationHeader.startsWith(JwtProvider.BEARER)) {
            log.debug("No JWT token found in request headers");
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authorizationHeader.substring(JwtProvider.BEARER.length());

        jwtProvider.validateToken(token);

        final String username = jwtProvider.getUsername(token);
        final Set<Authority> authorities = jwtProvider.getAuthority(token);
        final AuthDTO authDTO = new AuthDTO(username, authorities);

        final CustomUserDetails userDetails = new CustomUserDetails(authDTO);

        final Authentication authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
