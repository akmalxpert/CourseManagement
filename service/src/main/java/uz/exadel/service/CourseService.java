package uz.exadel.service;

import org.springframework.stereotype.Service;
import uz.exadel.dtos.ResponseData;

@Service
public interface CourseService {
    ResponseData getBySchoolId(String schoolId);
}
