package uz.exadel.service;

import org.springframework.stereotype.Service;
import uz.exadel.dtos.ResponseData;
import uz.exadel.dtos.TeacherDTO;

@Service
public interface TeacherService {
    ResponseData add(TeacherDTO teacherDTO);

    ResponseData get(String id);

    ResponseData delete(String id);

    ResponseData update(TeacherDTO teacherDTO, String id);

    ResponseData getBySchoolId(String schoolId);
}
