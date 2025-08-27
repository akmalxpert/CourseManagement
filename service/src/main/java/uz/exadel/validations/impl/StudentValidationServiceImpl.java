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
    private static final Logger logger = LogManager.getLogger(StudentValidationServiceImpl.class);

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
            logger.debug("Validating group ID for StudentDTO");
            ValidatorUtils.validateId(groupId);
        } else if (level != null && (level < 1 || level > 11)) {
            throw new ValidationException("Invalid level (1-11)");
        }
        // Allow both to be null - this will return all students
    }

    @Override
    public void validateDelete(String id) {
        ValidatorUtils.validateId(id);
    }

    @Override
    public void validateUpdate(String id, StudentDTO studentDTO) {
        ValidatorUtils.validateId(id);
        updateValidate(studentDTO);
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

    private void updateValidate(StudentDTO studentDTO) {
        // For updates, all fields are optional, but if provided, they must be valid

        // Validate full name if provided
        if (studentDTO.getFullName() != null) {
            ValidatorUtils.checkNullableAndMaxLength(studentDTO.getFullName(), 25, "Full Name");
        }

        // Validate group ID if provided
        if (studentDTO.getGroupId() != null) {
            logger.info("Validating groupId from Student DTO for update");
            ValidatorUtils.validateId(studentDTO.getGroupId());
        }

        // Validate courses if provided (courses can be null/empty for updates)
        if (studentDTO.getCourses() != null && !studentDTO.getCourses().isEmpty()) {
            logger.info("Start: Validating course IDs from student DTO for update");
            studentDTO.getCourses().forEach(ValidatorUtils::validateId);
            logger.info("Finish: Validating course IDs from student DTO for update");
        }

        // Validate level if provided
        if (studentDTO.getLevel() != null && (studentDTO.getLevel() < 1 || studentDTO.getLevel() > 11)) {
            throw new ValidationException("Invalid level (1-11)");
        }

    }
}
