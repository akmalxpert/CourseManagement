package uz.exadel.validations.impl;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import uz.exadel.dtos.SchoolDTO;
import uz.exadel.exception.MissingMandatoryFieldException;
import uz.exadel.utils.ValidatorUtils;
import uz.exadel.validations.SchoolValidationService;

@Service
public class SchoolValidationServiceImpl implements SchoolValidationService {

    @Override
    public void validateCreateSchool(SchoolDTO schoolDTO) {
        commonValidate(schoolDTO);
        additionalValidation(schoolDTO);
    }

    @Override
    public void validateUpdateSchool(String id, SchoolDTO schoolDTO) {
        ValidatorUtils.validateId(id);
        commonValidate(schoolDTO);
        additionalValidation(schoolDTO);
    }

    @Override
    public void validateDeleteSchool(String id) {
        ValidatorUtils.validateId(id);
    }

    @Override
    public void validateGetSchoolById(String id) {
        ValidatorUtils.validateId(id);
    }

    private void commonValidate(SchoolDTO schoolDTO) {
        if (!StringUtils.hasText(schoolDTO.getName())) {
            throw new MissingMandatoryFieldException("School name");
        }

        if (!StringUtils.hasText(schoolDTO.getAddress())) {
            throw new MissingMandatoryFieldException("School address");
        }

        if (!StringUtils.hasText(schoolDTO.getPhoneNumber())) {
            throw new MissingMandatoryFieldException("School phone number");
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
