package uz.exadel.service.impl;

import org.springframework.stereotype.Service;
import uz.exadel.dtos.GroupDTO;
import uz.exadel.dtos.ResponseData;
import uz.exadel.repository.GroupRepository;
import uz.exadel.service.GroupService;

import javax.transaction.Transactional;

@Service
@Transactional
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;

    public GroupServiceImpl(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }


    @Override
    public ResponseData add(GroupDTO groupDTO) {
        return null;
    }

    @Override
    public ResponseData get(String id) {
        return null;
    }

    @Override
    public ResponseData delete(String id) {
        return null;
    }

    @Override
    public ResponseData update(GroupDTO groupDTO, String id) {
        return null;
    }

    @Override
    public ResponseData getBySchoolIdAndFaculty(String schoolId, String faculty) {
        return null;
    }
}
