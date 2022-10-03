package uz.exadel.validations;

import uz.exadel.dtos.CourseDTO;

public interface CourseValidationService {
    void validateCreate(CourseDTO courseDTO);

    void validateGetById(String id);

    void validateGetBySchoolIdOrGroupId(String schoolId, String groupId);

    void validateDelete(String id);

    void validateUpdate(String id, CourseDTO courseDTO);
}
