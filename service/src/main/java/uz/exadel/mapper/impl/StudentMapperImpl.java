package uz.exadel.mapper.impl;

import org.springframework.stereotype.Service;
import uz.exadel.dtos.StudentDTO;
import uz.exadel.entity.Course;
import uz.exadel.entity.Student;
import uz.exadel.mapper.StudentMapper;
import uz.exadel.repository.CourseRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StudentMapperImpl implements StudentMapper {
    private final CourseRepository courseRepository;

    public StudentMapperImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public Student studentFromStudentDTO(StudentDTO studentDTO) {
        Student student = new Student();
        student.setFullName(studentDTO.getFullName());
        student.setLevel(studentDTO.getLevel());
        student.setGroupId(UUID.fromString(studentDTO.getGroupId()));

        List<String> courseIds = studentDTO.getCourses();
        Set<UUID> idSet = courseIds.stream().map(UUID::fromString).collect(Collectors.toSet());
        Set<Course> courses = courseRepository.findByIdIn(idSet);
        student.setCourses(courses);

        return student;
    }
}
