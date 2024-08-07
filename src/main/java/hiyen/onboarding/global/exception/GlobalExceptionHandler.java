package hiyen.onboarding.global.exception;

import lombok.extern.slf4j.Slf4j;
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
        Object rejectedValue = exception.getBindingResult().getFieldError().getRejectedValue();
        log.warn(String.format(defaultMessage + " [입력된 값 : %s]", rejectedValue));

        return ResponseEntity.badRequest().body(new ErrorResponse(defaultMessage));
    }
}
