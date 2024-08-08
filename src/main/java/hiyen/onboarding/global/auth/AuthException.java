package hiyen.onboarding.global.auth;

public class AuthException extends RuntimeException {

    private AuthException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public static class FailAuthenticationUserException extends AuthException {

        public FailAuthenticationUserException(final Throwable cause) {
            super("인증되지 않은 사용자의 접근입니다.", cause);
        }
    }
}
