package uz.exadel.validations.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import uz.exadel.dtos.GroupDTO;
import uz.exadel.enums.FacultyEnum;
import uz.exadel.exception.MissingMandatoryFieldException;
import uz.exadel.exception.ValidationException;
import uz.exadel.utils.ValidatorUtils;
import uz.exadel.validations.GroupValidationService;

@Service
public class GroupValidationServiceImpl implements GroupValidationService {
    private static final Logger logger = LogManager.getLogger(GroupValidationServiceImpl.class);

    @Override
    public void validateCreateGroup(GroupDTO groupDTO) {
        commonValidate(groupDTO);
    }

    @Override
    public void validateGetGroupBySchoolIdAndFaculty(String schoolId, String faculty) {
        if (schoolId != null) {
            logger.debug("Validating schoolId");
            ValidatorUtils.validateId(schoolId);
        }
        // Allow schoolId to be null - this will return all groups
        if (faculty != null) {
            validateFaculty(faculty);
        }
    }

    @Override
    public void validateGetGroupsByGroupIdInTheSameSchool(String groupId) {
        logger.info("Validating the groupId to get the list of groups in the same school");
        ValidatorUtils.validateId(groupId);
    }

    @Override
    public void validateUpdateGroup(String id, GroupDTO groupDTO) {
        ValidatorUtils.validateId(id);
        commonValidate(groupDTO);
    }

    @Override
    public void validateDeleteGroup(String id) {
        ValidatorUtils.validateId(id);
    }

    @Override
    public void validateGetGroupById(String id) {
        ValidatorUtils.validateId(id);
    }

    private void commonValidate(GroupDTO groupDTO) {
        if (!StringUtils.hasText(groupDTO.getName())) {
            throw new MissingMandatoryFieldException("Group Name");
        }

        if (groupDTO.getLevel() == null) {
            throw new MissingMandatoryFieldException("Group Level");
        }

        if (groupDTO.getLevel() < 1 || groupDTO.getLevel() > 11) {
            throw new ValidationException("Invalid group level (1-11)");
        }

        if (!StringUtils.hasText(groupDTO.getSchoolId())) {
            throw new MissingMandatoryFieldException("SchoolId");
        }

        logger.info("Validating School ID of groupDTO");
        ValidatorUtils.validateId(groupDTO.getSchoolId());

        validateFaculty(groupDTO.getFaculty());
    }

    private void validateFaculty(String faculty) {
        if (!StringUtils.hasText(faculty)) {
            throw new MissingMandatoryFieldException("Faculty");
        }

        try {
            FacultyEnum.valueOf(faculty);
        } catch (IllegalArgumentException exception) {
            throw new ValidationException("This Faculty is not valid for the system");
        }
    }
}
