package uz.exadel.service.impl;

import org.springframework.stereotype.Service;
import uz.exadel.dtos.ResponseData;
import uz.exadel.entity.Course;
import uz.exadel.exception.SchoolNotFoundException;
import uz.exadel.repository.CourseRepository;
import uz.exadel.repository.SchoolRepository;
import uz.exadel.service.CourseService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final SchoolRepository schoolRepository;

    public CourseServiceImpl(CourseRepository courseRepository, SchoolRepository schoolRepository) {
        this.courseRepository = courseRepository;
        this.schoolRepository = schoolRepository;
    }

    @Override
    public ResponseData getBySchoolId(String schoolId) {
        UUID schoolUUID = UUID.fromString(schoolId);
        schoolRepository.findById(schoolUUID).orElseThrow(SchoolNotFoundException::new);

        List<Course> courseList = courseRepository.findAllBySchoolId(schoolUUID);
        return new ResponseData(courseList);
    }
}
