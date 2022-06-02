package uz.exadel.exception;

/**
 * 400 - BadRequestException
 */
public class MissingMandatoryFieldException extends RuntimeException {

    public MissingMandatoryFieldException(String message) {
        super(message);
    }

}
