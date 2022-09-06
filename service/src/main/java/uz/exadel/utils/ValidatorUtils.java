package uz.exadel.utils;

import org.springframework.util.StringUtils;
import uz.exadel.exception.MissingMandatoryFieldException;
import uz.exadel.exception.ValidationException;

import java.util.UUID;

public class ValidatorUtils {
    public static void checkMaxLength(String value, int maxLength, String attributeName) {
        if (!StringUtils.hasText(value)) {
            throw new MissingMandatoryFieldException(uz.exadel.utils.StringUtils.capitalize(attributeName) + " is missing or empty");
        }
        if (value.length() > maxLength) {
            throw new ValidationException(uz.exadel.utils.StringUtils.capitalize(attributeName) + " should not be greater than " + maxLength + " characters");
        }
    }

    public static void validateId(String id) {
        if (!StringUtils.hasText(id)) {
            throw new MissingMandatoryFieldException("Id");
        }
        try {
            UUID uuid = UUID.fromString(id);
        } catch (IllegalArgumentException ex) {
            throw new ValidationException("This ID is not valid for the system");
        }
    }
}
