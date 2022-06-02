package uz.exadel.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExceptionResponse {
    private int code;
    private String status;
    private String message;
    private Object error;

    public ExceptionResponse(int code, String status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

    public ExceptionResponse(int code, String status, Object error) {
        this.code = code;
        this.status = status;
        this.error = error;
    }
}
