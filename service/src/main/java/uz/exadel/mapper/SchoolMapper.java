package uz.exadel.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import uz.exadel.dtos.SchoolDTO;
import uz.exadel.entity.School;

@Mapper
public interface SchoolMapper {
    SchoolMapper INSTANCE = Mappers.getMapper(SchoolMapper.class);

    School schoolToSchoolDTO(SchoolDTO schoolDTO);
}
