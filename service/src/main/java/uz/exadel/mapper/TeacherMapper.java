package uz.exadel.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.util.StringUtils;
import uz.exadel.dtos.TeacherDTO;
import uz.exadel.entity.Course;
import uz.exadel.entity.Teacher;
import uz.exadel.enums.TeacherPositionEnum;
import uz.exadel.repository.CourseRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class TeacherMapper {
    public static final TeacherMapper INSTANCE = Mappers.getMapper(TeacherMapper.class);

    protected final CourseRepository courseRepository;

    protected TeacherMapper(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Mapping(source = "schoolId", target = "schoolId", qualifiedByName = "stringToUUID")
    @Mapping(source = "courses", target = "courses", qualifiedByName = "stringListToCoursesSet")
    @Mapping(source = "positions", target = "positions", qualifiedByName = "stringListToPositionEnumSet")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "school", ignore = true)
    public abstract Teacher teacherFromTeacherDTO(TeacherDTO teacherDTO);

    @Mapping(source = "schoolId", target = "schoolId", qualifiedByName = "stringToUUID")
    @Mapping(source = "courses", target = "courses", qualifiedByName = "stringListToCoursesSet")
    @Mapping(source = "positions", target = "positions", qualifiedByName = "stringListToPositionEnumSet")
    @Mapping(source = "email", target = "email", qualifiedByName = "conditionalStringMapping")
    @Mapping(source = "officePhoneNumber", target = "officePhoneNumber", qualifiedByName = "conditionalStringMapping")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "school", ignore = true)
    public abstract Teacher teacherFromTeacherDTOUpdate(TeacherDTO teacherDTO, @MappingTarget Teacher teacher);

    @Named("stringToUUID")
    protected UUID stringToUUID(String id) {
        return UUID.fromString(id);
    }

    @Named("stringListToCoursesSet")
    protected Set<Course> stringListToCoursesSet(List<String> courseIds) {
        Set<UUID> courseUUIDs = courseIds.stream().map(UUID::fromString).collect(Collectors.toSet());
        return courseRepository.findByIdIn(courseUUIDs);
    }

    @Named("stringListToPositionEnumSet")
    protected Set<TeacherPositionEnum> stringListToPositionEnumSet(List<String> positions) {
        if (positions == null || positions.isEmpty()) {
            return null;
        }
        return positions.stream().map(TeacherPositionEnum::valueOf).collect(Collectors.toSet());
    }

    @Named("conditionalStringMapping")
    protected String conditionalStringMapping(String value) {
        return StringUtils.hasText(value) ? value : null;
    }
}