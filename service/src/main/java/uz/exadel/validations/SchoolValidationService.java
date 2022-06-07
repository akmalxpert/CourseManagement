package uz.exadel.validations;

import uz.exadel.dtos.SchoolDTO;

public interface SchoolValidationService {

    void validateCreateSchool(SchoolDTO schoolDTO);

    void validateUpdateSchool(String id, SchoolDTO schoolDTO);

    void validateDeleteSchool(String id);

    void validateGetSchoolById(String id);
}
