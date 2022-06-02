package uz.exadel.validations;

import uz.exadel.dtos.SchoolDTO;

public interface SchoolValidationService {

    void validateCreateSchool(SchoolDTO schoolDTO);

    void validateUpdateSchool(SchoolDTO schoolDTO);
}
