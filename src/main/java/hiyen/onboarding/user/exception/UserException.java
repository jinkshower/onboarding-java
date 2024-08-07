package hiyen.onboarding.user.exception;

public class UserException extends RuntimeException {

    private UserException(final String message) {
        super(message);
    }

    public static class FailLoginException extends UserException {

        public FailLoginException() {
            super("User not found");
        }
    }

    public static class NotFoundAuthorityException extends UserException {

        public NotFoundAuthorityException() {
            super("Authority not found");
        }
    }

    public static class DuplicatedUserException extends UserException {

        public DuplicatedUserException() {
            super("User already exists");
        }
    }
}
