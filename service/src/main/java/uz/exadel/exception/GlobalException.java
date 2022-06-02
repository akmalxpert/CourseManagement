package uz.exadel.exception;

import lombok.Data;

@Data
public class GlobalException extends RuntimeException {
    private int code;
    private String status;

    public GlobalException (String message, String status, int code) {
        super(message);
        this.code = code;
        this.status = status;
    }
}
