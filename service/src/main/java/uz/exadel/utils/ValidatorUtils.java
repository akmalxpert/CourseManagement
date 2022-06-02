package uz.exadel.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;
import uz.exadel.exception.MissingMandatoryFieldException;
import uz.exadel.exception.ValidationException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidatorUtils {
    public static void checkMaxLength(String value, int maxLength, String attributeName) {
        if (!StringUtils.hasText(value)) {
            throw new MissingMandatoryFieldException(uz.exadel.utils.StringUtils.capitalize(attributeName) + " is missing or empty");
        }
        if (value.length() > maxLength) {
            throw new ValidationException(uz.exadel.utils.StringUtils.capitalize(attributeName) + " should not be greater than " + maxLength + " characters");
        }
    }

}
