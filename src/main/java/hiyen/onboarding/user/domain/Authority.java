package hiyen.onboarding.user.domain;

import hiyen.onboarding.user.exception.UserException.NotFoundAuthorityException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class Authority {

    @Id
    @Column(name = "authority_name", length = 50)
    @Enumerated(EnumType.STRING)
    private UserAuthority authority;

    public enum UserAuthority {
        ROLE_USER,
        ROLE_ADMIN;

        public static UserAuthority of(String authorityName) {
            return Arrays.stream(values())
                    .filter(it -> it.name().equals(authorityName))
                    .findFirst()
                    .orElseThrow(NotFoundAuthorityException::new);
        }
    }
}
