package uz.exadel.validations;

import uz.exadel.dtos.GroupDTO;

public interface GroupValidationService {
    void validateCreateGroup(GroupDTO groupDTO);

    void validateUpdateGroup(String id, GroupDTO groupDTO);

    void validateDeleteGroup(String id);

    void validateGetGroupById(String id);
}
