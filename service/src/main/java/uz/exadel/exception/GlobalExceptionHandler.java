package uz.exadel.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {MissingMandatoryFieldException.class})
    @ResponseStatus(BAD_REQUEST)
    public HttpEntity<?> handleMissingMandatoryFieldException(MissingMandatoryFieldException ex) {
        ex.printStackTrace();
        return ResponseEntity.status(BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(value = {ValidationException.class})
    @ResponseStatus(BAD_REQUEST)
    public HttpEntity<?> handleValidationException(ValidationException ex) {
        ex.printStackTrace();
        return ResponseEntity.status(BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(value = {SchoolNotFoundException.class})
    @ResponseStatus(NOT_FOUND)
    public HttpEntity<?> handleSchoolNotFoundException(SchoolNotFoundException ex) {
        ex.printStackTrace();
        return ResponseEntity.status(NOT_FOUND).body("School not found");
    }

    @ExceptionHandler(value = {Exception.class})
    public HttpEntity<?> handleException(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong. Keep calm");
    }

}
