package hiyen.onboarding.global.config;

import hiyen.onboarding.global.auth.JwtAuthFilter;
import hiyen.onboarding.global.auth.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtProvider jwtProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(auth -> auth.disable())
                .httpBasic(auth -> auth.disable())
                .authorizeHttpRequests(
                        authorizeHttp -> {
                            authorizeHttp.requestMatchers("/api/users/*", "/error").permitAll();
                            authorizeHttp.anyRequest().authenticated();
                        }
                )
                .addFilterBefore(new JwtAuthFilter(jwtProvider), AuthorizationFilter.class)
                .build();
    }

}
