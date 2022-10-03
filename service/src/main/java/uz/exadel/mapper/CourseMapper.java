package uz.exadel.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import uz.exadel.dtos.CourseDTO;
import uz.exadel.entity.Course;

import java.util.UUID;

@Mapper
public interface CourseMapper {
    CourseMapper INSTANCE = Mappers.getMapper(CourseMapper.class);

    @Mapping(source = "schoolId", target = "schoolId", qualifiedByName = "stringToUUID")
    Course courseToCourseDTO(CourseDTO courseDTO);

    @Named("stringToUUID")
    public static UUID stringToUUID(String schoolId) {
        return UUID.fromString(schoolId);
    }
}
