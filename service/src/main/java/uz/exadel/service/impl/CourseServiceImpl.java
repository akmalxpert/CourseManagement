package uz.exadel.service.impl;

import org.springframework.stereotype.Service;
import uz.exadel.dtos.ResponseData;
import uz.exadel.entity.Course;
import uz.exadel.entity.Group;
import uz.exadel.exception.GroupNotFoundException;
import uz.exadel.exception.MissingMandatoryFieldException;
import uz.exadel.exception.SchoolNotFoundException;
import uz.exadel.repository.CourseRepository;
import uz.exadel.repository.GroupRepository;
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
    private final GroupRepository groupRepository;

    public CourseServiceImpl(CourseRepository courseRepository, SchoolRepository schoolRepository, GroupRepository groupRepository) {
        this.courseRepository = courseRepository;
        this.schoolRepository = schoolRepository;
        this.groupRepository = groupRepository;
    }

    @Override
    public ResponseData getBySchoolIdOrGroupId(String schoolId, String groupId) {
        UUID schoolUUID = null;
        if (schoolId == null && groupId != null) {
            UUID groupUUID = UUID.fromString(groupId);
            Group group = groupRepository.findById(groupUUID).orElseThrow(GroupNotFoundException::new);
            schoolUUID = group.getSchoolId();
        } else if (schoolId != null && groupId == null) {
            schoolUUID = UUID.fromString(schoolId);
        } else {
            throw new MissingMandatoryFieldException("schoolId or groupId");
        }
        schoolRepository.findById(schoolUUID).orElseThrow(SchoolNotFoundException::new);

        List<Course> courseList = courseRepository.findAllBySchoolId(schoolUUID);
        return new ResponseData(courseList);
    }
}
