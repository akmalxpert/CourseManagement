package uz.exadel.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uz.exadel.dtos.ResponseData;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {MissingMandatoryFieldException.class})
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ResponseData> handleMissingMandatoryFieldException(MissingMandatoryFieldException ex) {
        ex.printStackTrace();
        ResponseData responseData = new ResponseData(null, ex.getMessage(), BAD_REQUEST.value());
        return ResponseEntity.status(BAD_REQUEST).body(responseData);
    }

    @ExceptionHandler(value = {ValidationException.class})
    @ResponseStatus(BAD_REQUEST)
    public HttpEntity<?> handleValidationException(ValidationException ex) {
        ex.printStackTrace();
        ResponseData responseData = new ResponseData(null, ex.getMessage(), BAD_REQUEST.value());
        return ResponseEntity.status(BAD_REQUEST).body(responseData);
    }

    @ExceptionHandler(value = {SchoolNotFoundException.class})
    @ResponseStatus(NOT_FOUND)
    public HttpEntity<?> handleSchoolNotFoundException(SchoolNotFoundException ex) {
        ex.printStackTrace();
        ResponseData responseData = new ResponseData(null, "School not found", NOT_FOUND.value());
        return ResponseEntity.status(NOT_FOUND).body(responseData);
    }

    @ExceptionHandler(value = {GroupNotFoundException.class})
    @ResponseStatus(NOT_FOUND)
    public HttpEntity<?> handleGroupNotFoundException(GroupNotFoundException ex) {
        ex.printStackTrace();
        ResponseData responseData = new ResponseData(null, "Group not found", NOT_FOUND.value());
        return ResponseEntity.status(NOT_FOUND).body(responseData);
    }

    @ExceptionHandler(value = {TeacherNotFoundException.class})
    @ResponseStatus(NOT_FOUND)
    public HttpEntity<?> handleTeacherNotFoundException(TeacherNotFoundException ex) {
        ex.printStackTrace();
        ResponseData responseData = new ResponseData(null, "Teacher not found", NOT_FOUND.value());
        return ResponseEntity.status(NOT_FOUND).body(responseData);
    }

    @ExceptionHandler(value = {StudentNotFoundException.class})
    @ResponseStatus(NOT_FOUND)
    public HttpEntity<?> handleStudentNotFoundException(StudentNotFoundException ex) {
        ex.printStackTrace();
        ResponseData responseData = new ResponseData(null, "Student not found", NOT_FOUND.value());
        return ResponseEntity.status(NOT_FOUND).body(responseData);
    }

    @ExceptionHandler(value = {DataIntegrityViolationException.class})
    @ResponseStatus(BAD_REQUEST)
    public HttpEntity<?> handleConstraintViolationException(DataIntegrityViolationException ex) {
        ex.printStackTrace();
        ResponseData responseData = new ResponseData(null, "This operation affects some other entities. Please delete all related entities to avoid this error.", BAD_REQUEST.value());
        return ResponseEntity.status(BAD_REQUEST).body(responseData);
    }

    @ExceptionHandler(value = {EmptyResultDataAccessException.class})
    @ResponseStatus(BAD_REQUEST)
    public HttpEntity<?> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex) {
        ex.printStackTrace();
        ResponseData responseData = new ResponseData(null, ex.getMessage(), BAD_REQUEST.value());
        return ResponseEntity.status(BAD_REQUEST).body(responseData);
    }

    @ExceptionHandler(value = {Exception.class})
    public HttpEntity<?> handleException(Exception ex) {
        ex.printStackTrace();
        ResponseData responseData = new ResponseData(null, "Something went wrong. Keep calm", INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
    }

}
