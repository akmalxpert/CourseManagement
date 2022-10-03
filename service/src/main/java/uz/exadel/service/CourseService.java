package uz.exadel.service;

import org.springframework.stereotype.Service;
import uz.exadel.dtos.ResponseData;
import uz.exadel.dtos.CourseDTO;

@Service
public interface CourseService {
    ResponseData getBySchoolIdOrGroupId(String schoolId, String groupId);

    ResponseData add(CourseDTO courseDTO);

    ResponseData get(String id);

    ResponseData delete(String id);

    ResponseData update(String id, CourseDTO courseDTO);

}
