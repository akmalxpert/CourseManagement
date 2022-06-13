package uz.exadel.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uz.exadel.dtos.ResponseItem;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {MissingMandatoryFieldException.class})
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ResponseItem> handleMissingMandatoryFieldException(MissingMandatoryFieldException ex) {
        ex.printStackTrace();
        ResponseItem responseItem = new ResponseItem(null, ex.getMessage(), false);
        return ResponseEntity.status(BAD_REQUEST).body(responseItem);
    }

    @ExceptionHandler(value = {ValidationException.class})
    @ResponseStatus(BAD_REQUEST)
    public HttpEntity<?> handleValidationException(ValidationException ex) {
        ex.printStackTrace();
        ResponseItem responseItem = new ResponseItem(null, ex.getMessage(), false);
        return ResponseEntity.status(BAD_REQUEST).body(responseItem);
    }

    @ExceptionHandler(value = {SchoolNotFoundException.class})
    @ResponseStatus(NOT_FOUND)
    public HttpEntity<?> handleSchoolNotFoundException(SchoolNotFoundException ex) {
        ex.printStackTrace();
        ResponseItem responseItem = new ResponseItem(null, "School not found", false);
        return ResponseEntity.status(NOT_FOUND).body(responseItem);
    }

    @ExceptionHandler(value = {Exception.class})
    public HttpEntity<?> handleException(Exception ex) {
        ex.printStackTrace();
        ResponseItem responseItem = new ResponseItem(null, "Something went wrong. Keep calm", false);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseItem);
    }

}
