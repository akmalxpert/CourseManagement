package uz.exadel.validations.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import uz.exadel.dtos.CourseDTO;
import uz.exadel.exception.MissingMandatoryFieldException;
import uz.exadel.exception.ValidationException;
import uz.exadel.utils.ValidatorUtils;
import uz.exadel.validations.CourseValidationService;

@Service
public class CourseValidationServiceImpl implements CourseValidationService {
    private static final Logger logger = LogManager.getLogger(CourseValidationServiceImpl.class);

    @Override
    public void validateCreate(CourseDTO courseDTO) {
        commonValidate(courseDTO);
    }

    @Override
    public void validateGetById(String id) {
        ValidatorUtils.validateId(id);
    }

    @Override
    public void validateGetBySchoolIdOrGroupId(String schoolId, String groupId) {
        if (groupId != null && schoolId != null) {
            throw new ValidationException("Only groupId or schoolId should be provided");
        } else if (schoolId != null) {
            logger.info("Validating school ID for CourseDTO");
            ValidatorUtils.validateId(schoolId);
        } else if (groupId != null) {
            logger.info("Validating group ID for CourseDTO");
            ValidatorUtils.validateId(groupId);
        } else {
            throw new MissingMandatoryFieldException("GroupId or SchoolId");
        }
    }

    @Override
    public void validateDelete(String id) {
        ValidatorUtils.validateId(id);
    }

    @Override
    public void validateUpdate(String id, CourseDTO courseDTO) {
        ValidatorUtils.validateId(id);
        commonValidate(courseDTO);
    }

    private void commonValidate(CourseDTO courseDTO) {
        if (courseDTO.getCode() != null) {
            ValidatorUtils.checkNullableAndMaxLength(courseDTO.getCode(), 15, "Course Code");
        }
        logger.info("Validating schoolId from Course DTO");
        ValidatorUtils.validateId(courseDTO.getSchoolId());

        if (!StringUtils.hasText(courseDTO.getName())) {
            throw new MissingMandatoryFieldException("Course Name");
        }
    }
}
