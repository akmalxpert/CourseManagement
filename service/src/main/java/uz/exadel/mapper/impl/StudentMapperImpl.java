package uz.exadel.mapper.impl;

import org.springframework.stereotype.Service;
import uz.exadel.dtos.StudentDTO;
import uz.exadel.entity.Course;
import uz.exadel.entity.Student;
import uz.exadel.mapper.StudentMapper;
import uz.exadel.repository.CourseRepository;

import java.util.HashSet;
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
        return convert(studentDTO, student);
    }

    @Override
    public Student studentFromStudentDTOUpdate(StudentDTO studentDTO, Student student) {
        return partialUpdate(studentDTO, student);
    }

    private Student convert(StudentDTO studentDTO, Student student) {
        student.setFullName(studentDTO.getFullName());
        student.setLevel(studentDTO.getLevel());
        student.setGroupId(UUID.fromString(studentDTO.getGroupId()));

        List<String> courseIds = studentDTO.getCourses();
        Set<UUID> idSet = courseIds.stream().map(UUID::fromString).collect(Collectors.toSet());
        Set<Course> courses = courseRepository.findByIdIn(idSet);
        student.setCourses(courses);

        return student;
    }

    private Student partialUpdate(StudentDTO studentDTO, Student student) {
        // Only update fields that are actually provided (non-null)
        
        if (studentDTO.getFullName() != null) {
            student.setFullName(studentDTO.getFullName());
        }
        
        if (studentDTO.getLevel() != null) {
            student.setLevel(studentDTO.getLevel());
        }
        
        if (studentDTO.getGroupId() != null) {
            student.setGroupId(UUID.fromString(studentDTO.getGroupId()));
        }
        
        // Handle courses update with three scenarios:
        // 1. null = don't change existing courses
        // 2. empty list = explicitly remove all courses  
        // 3. non-empty list = update to new courses
        if (studentDTO.getCourses() != null) {
            if (studentDTO.getCourses().isEmpty()) {
                // Explicitly clear all courses
                student.setCourses(new HashSet<>());
            } else {
                // Update to new courses
                List<String> courseIds = studentDTO.getCourses();
                Set<UUID> idSet = courseIds.stream().map(UUID::fromString).collect(Collectors.toSet());
                Set<Course> courses = courseRepository.findByIdIn(idSet);
                student.setCourses(new HashSet<>(courses));
            }
        }
        // Note: If courses is null, we keep the existing courses unchanged
        
        return student;
    }
}
