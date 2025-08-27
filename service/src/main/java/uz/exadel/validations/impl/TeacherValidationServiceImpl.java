package uz.exadel.validations.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
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
    private static final Logger logger = LogManager.getLogger(TeacherValidationServiceImpl.class);

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
        if (schoolId != null) {
            logger.debug("Validating school ID for TeacherDTO");
            ValidatorUtils.validateId(schoolId);
        }
        // Allow schoolId to be null - this will return all teachers
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
        ValidatorUtils.checkNullableAndMaxLength(teacherDTO.getFullName(), 25, "Full Name");

        if (teacherDTO.getCourses() == null || teacherDTO.getCourses().isEmpty()) {
            throw new MissingMandatoryFieldException("Courses");
        }

        logger.info("Start: Validating course IDs from teacher DTO");
        teacherDTO.getCourses().forEach(ValidatorUtils::validateId);
        logger.info("Finish: Validating course IDs from teacher DTO");

        if (teacherDTO.getPositions() == null || teacherDTO.getPositions().isEmpty()) {
            throw new MissingMandatoryFieldException("Teacher Positions");
        }

        List<String> positions = teacherDTO.getPositions();
        try {
            positions.stream().map(TeacherPositionEnum::valueOf).collect(Collectors.toList());
        } catch (RuntimeException exception) {
            throw new ValidationException("Position(s) is not valid for the system");
        }

        logger.info("Validating schoolId from teacherDTO");
        ValidatorUtils.validateId(teacherDTO.getSchoolId());
    }
}
