package uz.exadel.validations.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import uz.exadel.dtos.StudentDTO;
import uz.exadel.exception.MissingMandatoryFieldException;
import uz.exadel.exception.ValidationException;
import uz.exadel.utils.ValidatorUtils;
import uz.exadel.validations.StudentValidationService;

@Service
public class StudentValidationServiceImpl implements StudentValidationService {
    private static final Logger logger = LogManager.getLogger(SchoolValidationServiceImpl.class);

    @Override
    public void validateCreate(StudentDTO studentDTO) {
        commonValidate(studentDTO);
    }

    @Override
    public void validateGetById(String id) {
        ValidatorUtils.validateId(id);
    }

    @Override
    public void validateGetByGroupIdOrLevel(String groupId, Integer level) {
        if (groupId != null && level != null) {
            throw new ValidationException("Only groupId or level should be provided");
        } else if (groupId != null) {
            logger.info("Validating group ID for StudentDTO");
            ValidatorUtils.validateId(groupId);
        } else if (level != null && (level < 1 || level > 11)) {
            throw new ValidationException("Invalid level (1-11)");
        } else {
            throw new MissingMandatoryFieldException("GroupId or Level");
        }
    }

    @Override
    public void validateDelete(String id) {
        ValidatorUtils.validateId(id);
    }

    @Override
    public void validateUpdate(String id, StudentDTO studentDTO) {
        ValidatorUtils.validateId(id);
        commonValidate(studentDTO);
    }

    private void commonValidate(StudentDTO studentDTO) {
        ValidatorUtils.checkNullableAndMaxLength(studentDTO.getFullName(), 25, "Full Name");

        logger.info("Validating groupId from Student DTO");
        ValidatorUtils.validateId(studentDTO.getGroupId());

        if (studentDTO.getCourses() == null || studentDTO.getCourses().isEmpty()) {
            throw new MissingMandatoryFieldException("Courses");
        }

        logger.info("Start: Validating course IDs from student DTO");
        studentDTO.getCourses().forEach(ValidatorUtils::validateId);
        logger.info("Finish: Validating course IDs from student DTO");

        if (studentDTO.getLevel() == null) {
            throw new MissingMandatoryFieldException("Student Level");
        }

        if (studentDTO.getLevel() < 1 || studentDTO.getLevel() > 11) {
            throw new ValidationException("Invalid level (1-11)");
        }
    }
}
