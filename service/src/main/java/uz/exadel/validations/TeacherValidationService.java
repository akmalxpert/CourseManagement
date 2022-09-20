package uz.exadel.validations;

import uz.exadel.dtos.TeacherDTO;

public interface TeacherValidationService {
    void validateCreate(TeacherDTO teacherDTO);
    void validateGetById(String id);
    void validateGetBySchoolId(String schoolId);
    void validateDelete(String id);
    void validateUpdate(TeacherDTO teacherDTO, String id);
}
