package uz.exadel.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import uz.exadel.dtos.GroupDTO;
import uz.exadel.entity.Group;

@Mapper
public interface GroupMapper {
    GroupMapper INSTANCE = Mappers.getMapper(GroupMapper.class);

    Group groupToGroupDTO(GroupDTO groupDTO);
}
