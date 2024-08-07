package hiyen.onboarding.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Authority {

    @Id
    @Column(name = "authority_name", length = 50)
    @Enumerated(EnumType.STRING)
    private UserAuthority authority;

    public enum UserAuthority {
        ROLE_USER,
        ROLE_ADMIN
    }
}
