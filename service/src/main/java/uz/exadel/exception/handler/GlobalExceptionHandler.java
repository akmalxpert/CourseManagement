package uz.exadel.exception.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uz.exadel.exception.ExceptionResponse;
import uz.exadel.exception.GlobalException;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<Object> handleGlobalException(GlobalException ex) {
        return ResponseEntity
                .status(ex.getCode())
                .body(new ExceptionResponse(
                                ex.getCode(),
                                ex.getStatus(),
                                ex.getMessage()
                        )
                );
    }

    @ExceptionHandler(value = {Exception.class})
    public HttpEntity<?> handleException(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong. Keep calm");
    }

}
