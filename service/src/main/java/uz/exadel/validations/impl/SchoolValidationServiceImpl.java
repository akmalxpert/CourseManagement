package uz.exadel.validations.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import uz.exadel.dtos.SchoolDTO;
import uz.exadel.exception.MissingMandatoryFieldException;
import uz.exadel.validations.SchoolValidationService;
import uz.exadel.utils.ValidatorUtils;

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
    public void validateUpdateSchool(SchoolDTO schoolDTO) {
        commonValidate(schoolDTO);
    }

    private void commonValidate(SchoolDTO schoolDTO) {
        if (StringUtils.hasText(schoolDTO.getName())) {
            throw new MissingMandatoryFieldException("School name is missing or empty");
        }

        if (StringUtils.hasText(schoolDTO.getAddress())) {
            throw new MissingMandatoryFieldException("School address is missing or empty");
        }

        if (StringUtils.hasText(schoolDTO.getPhoneNumber())) {
            throw new MissingMandatoryFieldException("School phone number is missing or empty");
        }
    }

    private void additionalValidation(SchoolDTO schoolDTO) {
        ValidatorUtils.checkMaxLength(schoolDTO.getName(), 25, "name");
        ValidatorUtils.checkMaxLength(schoolDTO.getPhoneNumber(), 12, "phone number");
    }

}
