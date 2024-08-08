package hiyen.onboarding.global.exception;

import hiyen.onboarding.global.auth.AuthException;
import hiyen.onboarding.user.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {
            MethodArgumentNotValidException.class
    })
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(final MethodArgumentNotValidException exception) {
        final String defaultMessage = exception.getBindingResult().getAllErrors().get(0)
                .getDefaultMessage();
        final Object rejectedValue = exception.getBindingResult().getFieldError().getRejectedValue();
        log.warn(String.format(defaultMessage + " [입력된 값 : %s]", rejectedValue));

        return ResponseEntity.badRequest().body(new ErrorResponse(defaultMessage));
    }

    @ExceptionHandler(value = {
            AuthException.FailAuthenticationUserException.class
    })
    public ResponseEntity<ErrorResponse> handleAuthException(final AuthException exception) {
        String message = exception.getMessage();
        log.warn("AuthException: {}", message, exception);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(message));
    }

    @ExceptionHandler(value = {
            UserException.FailLoginException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequestException(
            final RuntimeException exception) {
        String message = exception.getMessage();
        log.warn("BadRequestException: {}", message, exception);

        return ResponseEntity.badRequest().body(new ErrorResponse(message));
    }

    @ExceptionHandler(value = {
            UserException.NotFoundAuthorityException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFoundException(final RuntimeException exception) {
        String message = exception.getMessage();
        log.warn("NotFoundException: {}", message, exception);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(message));
    }
}
