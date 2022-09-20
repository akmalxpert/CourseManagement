package uz.exadel.validations.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import uz.exadel.dtos.TeacherDTO;
import uz.exadel.enums.TeacherPositionEnum;
import uz.exadel.exception.MissingMandatoryFieldException;
import uz.exadel.exception.ValidationException;
import uz.exadel.utils.ValidatorUtils;
import uz.exadel.validations.TeacherValidationService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeacherValidationServiceImpl implements TeacherValidationService {
    private static final Logger logger = LogManager.getLogger(SchoolValidationServiceImpl.class);

    @Override
    public void validateCreate(TeacherDTO teacherDTO) {
        commonValidate(teacherDTO);
    }

    @Override
    public void validateGetById(String id) {
        ValidatorUtils.validateId(id);
    }

    @Override
    public void validateGetBySchoolId(String schoolId) {
        logger.info("Validating school ID for TeacherDTO");
        ValidatorUtils.validateId(schoolId);
    }

    @Override
    public void validateDelete(String id) {
        ValidatorUtils.validateId(id);
    }

    @Override
    public void validateUpdate(TeacherDTO teacherDTO, String id) {
        ValidatorUtils.validateId(id);
        commonValidate(teacherDTO);
    }

    private void commonValidate(TeacherDTO teacherDTO) {
        if (!StringUtils.hasText(teacherDTO.getFullName())) {
            throw new MissingMandatoryFieldException("Full Name");
        }

        if (teacherDTO.getCourses().isEmpty()) {
            throw new MissingMandatoryFieldException("Courses");
        }

        List<String> positions = teacherDTO.getPositions();
        if (!positions.isEmpty()) {
            try {
                positions.stream().map(TeacherPositionEnum::valueOf).collect(Collectors.toList());
            } catch (RuntimeException exception) {
                throw new ValidationException("Position(s) are not valid for the system");
            }
        }

        ValidatorUtils.checkMaxLength(teacherDTO.getFullName(), 25, "Full Name");

        logger.info("Validating schoolId from teacherDTO");
        ValidatorUtils.validateId(teacherDTO.getSchoolId());
    }
}
