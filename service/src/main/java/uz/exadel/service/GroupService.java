package uz.exadel.service;

import org.springframework.stereotype.Service;
import uz.exadel.dtos.GroupDTO;
import uz.exadel.dtos.ResponseData;

@Service
public interface GroupService {
    ResponseData add(GroupDTO groupDTO);

    ResponseData get(String id);

    ResponseData delete(String id);

    ResponseData update(GroupDTO groupDTO, String id);

    ResponseData getBySchoolIdAndFaculty(String schoolId, String faculty);

    ResponseData getByGroupIdInTheSameSchool(String withGroupId);
}
