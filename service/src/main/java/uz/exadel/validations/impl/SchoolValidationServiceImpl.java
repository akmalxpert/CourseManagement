package uz.exadel.validations.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import uz.exadel.dtos.SchoolDTO;
import uz.exadel.exception.MissingMandatoryFieldException;
import uz.exadel.exception.ValidationException;
import uz.exadel.utils.ValidatorUtils;
import uz.exadel.validations.SchoolValidationService;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class SchoolValidationServiceImpl implements SchoolValidationService {

    @Override
    public void validateCreateSchool(SchoolDTO schoolDTO) {
        commonValidate(schoolDTO);
        additionalValidation(schoolDTO);
    }

    @Override
    public void validateUpdateSchool(String id, SchoolDTO schoolDTO) {
        validateId(id);
        commonValidate(schoolDTO);
        additionalValidation(schoolDTO);
    }

    @Override
    public void validateDeleteSchool(String id) {
        validateId(id);
    }

    @Override
    public void validateGetSchoolById(String id) {
        validateId(id);
    }

    private void validateId(String id) {
        if (!StringUtils.hasText(id)) {
            throw new MissingMandatoryFieldException("Id is missing or empty");
        }
        try {
            UUID uuid = UUID.fromString(id);
        } catch (IllegalArgumentException ex) {
            throw new ValidationException("This ID is not valid for the system");
        }
    }

    private void commonValidate(SchoolDTO schoolDTO) {
        if (!StringUtils.hasText(schoolDTO.getName())) {
            throw new MissingMandatoryFieldException("School name is missing or empty");
        }

        if (!StringUtils.hasText(schoolDTO.getAddress())) {
            throw new MissingMandatoryFieldException("School address is missing or empty");
        }

        if (!StringUtils.hasText(schoolDTO.getPhoneNumber())) {
            throw new MissingMandatoryFieldException("School phone number is missing or empty");
        }
    }

    private void additionalValidation(SchoolDTO schoolDTO) {
        ValidatorUtils.checkMaxLength(schoolDTO.getName(), 25, "name");
        ValidatorUtils.checkMaxLength(schoolDTO.getPhoneNumber(), 13, "phone number");
        if (StringUtils.hasText(schoolDTO.getPostalCode())) {
            ValidatorUtils.checkMaxLength(schoolDTO.getPostalCode(), 7, "postal code");
        }
    }

}
