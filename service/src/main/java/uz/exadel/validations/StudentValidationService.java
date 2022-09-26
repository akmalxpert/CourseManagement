package uz.exadel.validations;

import uz.exadel.dtos.StudentDTO;

public interface StudentValidationService {
    void validateCreate(StudentDTO studentDTO);

    void validateGetById(String id);

    void validateGetByGroupIdOrLevel(String groupId, Integer level);

    void validateDelete(String id);

    void validateUpdate(String id, StudentDTO studentDTO);
}
