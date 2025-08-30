package uz.exadel.mapper;

import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import uz.exadel.dtos.StudentDTO;
import uz.exadel.entity.Course;
import uz.exadel.entity.Student;
import uz.exadel.repository.CourseRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public abstract class StudentMapper {

    @Autowired
    private CourseRepository courseRepository;

    @Mapping(source = "groupId", target = "groupId", qualifiedByName = "stringToUUID")
    @Mapping(source = "courses", target = "courses", qualifiedByName = "stringListToCoursesSet")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "group", ignore = true)
    public abstract Student studentFromStudentDTO(StudentDTO studentDTO);

    @Mapping(source = "groupId", target = "groupId", qualifiedByName = "stringToUUID")
    @Mapping(source = "courses", target = "courses", qualifiedByName = "stringListToCoursesSet")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "group", ignore = true)
    public abstract Student studentFromStudentDTOUpdate(StudentDTO studentDTO, @MappingTarget Student student);

    @Named("stringToUUID")
    protected UUID stringToUUID(String id) {
        return UUID.fromString(id);
    }

    @Named("stringListToCoursesSet")
    protected Set<Course> stringListToCoursesSet(List<String> courseIds) {
        Set<UUID> idSet = courseIds.stream().map(UUID::fromString).collect(Collectors.toSet());
        return courseRepository.findByIdIn(idSet);
    }
}
