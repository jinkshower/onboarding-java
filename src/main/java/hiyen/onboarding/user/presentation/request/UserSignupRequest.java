package hiyen.onboarding.user.presentation.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserSignupRequest(

        @Size(min = 4, max = 20, message = "아이디는 4자 이상 20자 이하여야 합니다.")
        String username,
        @Pattern(regexp = PASSWORD_REGEX, message = "비밀번호는 4자 이상, 소문자, 대문자, 숫자의 조합이어야 합니다.")
        String password,
        @Size(min = 2, max = 10, message = "닉네임은 2자 이상 10자 이하여야 합니다.")
        String nickname
) {

    private static final String PASSWORD_REGEX = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{4,}$";
}
