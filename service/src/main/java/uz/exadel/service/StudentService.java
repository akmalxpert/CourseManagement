package uz.exadel.service;

import org.springframework.stereotype.Service;
import uz.exadel.dtos.ResponseData;
import uz.exadel.dtos.StudentDTO;

@Service
public interface StudentService {
    ResponseData add(StudentDTO studentDTO);

    ResponseData get(String id);

    ResponseData delete(String id);

    ResponseData update(String id, StudentDTO studentDTO);

    ResponseData getByGroupIdOrLevel(String groupId, Integer level);
}
