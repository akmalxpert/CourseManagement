package uz.exadel.service.impl;

import org.springframework.stereotype.Service;
import uz.exadel.dtos.GroupDTO;
import uz.exadel.dtos.ResponseData;
import uz.exadel.entity.Group;
import uz.exadel.enums.FacultyEnum;
import uz.exadel.exception.GroupNotFoundException;
import uz.exadel.exception.SchoolNotFoundException;
import uz.exadel.mapper.GroupMapper;
import uz.exadel.repository.GroupRepository;
import uz.exadel.repository.SchoolRepository;
import uz.exadel.service.GroupService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Transactional
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;
    private final SchoolRepository schoolRepository;

    public GroupServiceImpl(GroupRepository groupRepository, SchoolRepository schoolRepository) {
        this.groupRepository = groupRepository;
        this.schoolRepository = schoolRepository;
    }

    @Override
    public ResponseData add(GroupDTO groupDTO) {
        String schoolId = groupDTO.getSchoolId();
        schoolRepository.findById(UUID.fromString(schoolId)).orElseThrow(SchoolNotFoundException::new);

        Group group = GroupMapper.INSTANCE.groupToGroupDTO(groupDTO);
        groupRepository.save(group);
        return new ResponseData(null, "Save success");
    }

    @Override
    public ResponseData get(String id) {
        Group group = groupRepository.findById(UUID.fromString(id)).orElseThrow(GroupNotFoundException::new);
        return new ResponseData(group);
    }

    @Override
    public ResponseData delete(String id) {
        UUID uuid = UUID.fromString(id);
        groupRepository.findById(uuid).orElseThrow(GroupNotFoundException::new);

        groupRepository.deleteById(uuid);
        return new ResponseData(null, "Delete success");
    }

    @Override
    public ResponseData update(GroupDTO groupDTO, String id) {
        UUID uuid = UUID.fromString(id);
        Group group = groupRepository.findById(uuid).orElseThrow(GroupNotFoundException::new);

        UUID schoolUUID = UUID.fromString(groupDTO.getSchoolId());
        if (!Objects.equals(group.getSchoolId(), schoolUUID)) {
            schoolRepository.findById(schoolUUID)
                    .orElseThrow(SchoolNotFoundException::new);
        }

        group = GroupMapper.INSTANCE.groupToGroupDTO(groupDTO);
        group.setId(uuid);

        groupRepository.save(group);

        return new ResponseData(null, "Update success");
    }

    @Override
    public ResponseData getBySchoolIdAndFaculty(String schoolId, String faculty) {
        UUID schoolUUID = UUID.fromString(schoolId);
        schoolRepository.findById(schoolUUID)
                .orElseThrow(SchoolNotFoundException::new);

        List<Group> groups;
        if (faculty != null) {
            groups = groupRepository.findBySchoolIdAndFaculty(schoolUUID, FacultyEnum.valueOf(faculty));
        } else {
            groups = groupRepository.findBySchoolId(schoolUUID);
        }

        return new ResponseData(groups);
    }
}
