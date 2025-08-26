package uz.exadel.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import uz.exadel.dtos.ResponseData;

import java.util.UUID;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = {MissingMandatoryFieldException.class})
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ResponseData> handleMissingMandatoryFieldException(
            MissingMandatoryFieldException ex, WebRequest request) {
        String errorId = generateErrorId();
        logger.warn("Validation error [{}]: {} - Request: {}", 
                errorId, ex.getMessage(), getRequestInfo(request));
        
        ResponseData responseData = new ResponseData(null, ex.getMessage(), BAD_REQUEST.value());
        return ResponseEntity.status(BAD_REQUEST).body(responseData);
    }

    @ExceptionHandler(value = {ValidationException.class})
    @ResponseStatus(BAD_REQUEST)
    public HttpEntity<?> handleValidationException(ValidationException ex, WebRequest request) {
        String errorId = generateErrorId();
        logger.warn("Validation error [{}]: {} - Request: {}", 
                errorId, ex.getMessage(), getRequestInfo(request));
        
        ResponseData responseData = new ResponseData(null, ex.getMessage(), BAD_REQUEST.value());
        return ResponseEntity.status(BAD_REQUEST).body(responseData);
    }

    @ExceptionHandler(value = {SchoolNotFoundException.class})
    @ResponseStatus(NOT_FOUND)
    public HttpEntity<?> handleSchoolNotFoundException(SchoolNotFoundException ex, WebRequest request) {
        String errorId = generateErrorId();
        logger.warn("Entity not found [{}]: School not found - Request: {}", 
                errorId, getRequestInfo(request));
        
        ResponseData responseData = new ResponseData(null, "School not found", NOT_FOUND.value());
        return ResponseEntity.status(NOT_FOUND).body(responseData);
    }

    @ExceptionHandler(value = {GroupNotFoundException.class})
    @ResponseStatus(NOT_FOUND)
    public HttpEntity<?> handleGroupNotFoundException(GroupNotFoundException ex, WebRequest request) {
        String errorId = generateErrorId();
        logger.warn("Entity not found [{}]: Group not found - Request: {}", 
                errorId, getRequestInfo(request));
        
        ResponseData responseData = new ResponseData(null, "Group not found", NOT_FOUND.value());
        return ResponseEntity.status(NOT_FOUND).body(responseData);
    }

    @ExceptionHandler(value = {CourseNotFoundException.class})
    @ResponseStatus(NOT_FOUND)
    public HttpEntity<?> handleCourseNotFoundException(CourseNotFoundException ex, WebRequest request) {
        String errorId = generateErrorId();
        logger.warn("Entity not found [{}]: Course not found - Request: {}", 
                errorId, getRequestInfo(request));
        
        ResponseData responseData = new ResponseData(null, "Course not found", NOT_FOUND.value());
        return ResponseEntity.status(NOT_FOUND).body(responseData);
    }

    @ExceptionHandler(value = {TeacherNotFoundException.class})
    @ResponseStatus(NOT_FOUND)
    public HttpEntity<?> handleTeacherNotFoundException(TeacherNotFoundException ex, WebRequest request) {
        String errorId = generateErrorId();
        logger.warn("Entity not found [{}]: Teacher not found - Request: {}", 
                errorId, getRequestInfo(request));
        
        ResponseData responseData = new ResponseData(null, "Teacher not found", NOT_FOUND.value());
        return ResponseEntity.status(NOT_FOUND).body(responseData);
    }

    @ExceptionHandler(value = {StudentNotFoundException.class})
    @ResponseStatus(NOT_FOUND)
    public HttpEntity<?> handleStudentNotFoundException(StudentNotFoundException ex, WebRequest request) {
        String errorId = generateErrorId();
        logger.warn("Entity not found [{}]: Student not found - Request: {}", 
                errorId, getRequestInfo(request));
        
        ResponseData responseData = new ResponseData(null, "Student not found", NOT_FOUND.value());
        return ResponseEntity.status(NOT_FOUND).body(responseData);
    }

    @ExceptionHandler(value = {DataIntegrityViolationException.class})
    @ResponseStatus(BAD_REQUEST)
    public HttpEntity<?> handleConstraintViolationException(DataIntegrityViolationException ex, WebRequest request) {
        String errorId = generateErrorId();
        logger.warn("Data integrity violation [{}]: {} - Request: {}", 
                errorId, ex.getMessage(), getRequestInfo(request));
        
        ResponseData responseData = new ResponseData(null, 
                "This operation affects some other entities. Please delete all related entities to avoid this error.", 
                BAD_REQUEST.value());
        return ResponseEntity.status(BAD_REQUEST).body(responseData);
    }

    @ExceptionHandler(value = {EmptyResultDataAccessException.class})
    @ResponseStatus(BAD_REQUEST)
    public HttpEntity<?> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex, WebRequest request) {
        String errorId = generateErrorId();
        logger.warn("Empty result data access [{}]: {} - Request: {}", 
                errorId, ex.getMessage(), getRequestInfo(request));
        
        ResponseData responseData = new ResponseData(null, "The requested resource was not found.", BAD_REQUEST.value());
        return ResponseEntity.status(BAD_REQUEST).body(responseData);
    }

    @ExceptionHandler(value = {Exception.class})
    public HttpEntity<?> handleException(Exception ex, WebRequest request) {
        String errorId = generateErrorId();
        logger.error("Unexpected error [{}]: {} - Request: {} - Stack trace: ", 
                errorId, ex.getMessage(), getRequestInfo(request), ex);
        
        ResponseData responseData = new ResponseData(null, 
                "An unexpected error occurred. Please contact support with error ID: " + errorId, 
                INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseData);
    }

    /**
     * Generates a unique error ID for tracking purposes
     */
    private String generateErrorId() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * Extracts relevant request information for logging
     */
    private String getRequestInfo(WebRequest request) {
        String method = request.getHeader("X-HTTP-Method-Override");
        if (method == null) {
            method = "Unknown";
        }
        String uri = request.getDescription(false);
        return String.format("%s %s", method, uri);
    }

}
