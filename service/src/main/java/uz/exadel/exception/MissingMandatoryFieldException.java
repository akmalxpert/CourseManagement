package uz.exadel.exception;

/**
 * 400 - BadRequestException
 */
public class MissingMandatoryFieldException extends RuntimeException {

    public MissingMandatoryFieldException(String fieldName) {
        super(fieldName + " is missing or empty");
    }

}
