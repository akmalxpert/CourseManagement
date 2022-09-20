package uz.exadel.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import uz.exadel.dtos.ResponseData;
import uz.exadel.dtos.TeacherDTO;
import uz.exadel.entity.Course;
import uz.exadel.entity.Teacher;
import uz.exadel.enums.TeacherPositionEnum;
import uz.exadel.exception.SchoolNotFoundException;
import uz.exadel.exception.TeacherNotFoundException;
import uz.exadel.mapper.TeacherMapper;
import uz.exadel.repository.CourseRepository;
import uz.exadel.repository.SchoolRepository;
import uz.exadel.repository.TeacherRepository;
import uz.exadel.service.TeacherService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class TeacherServiceImpl implements TeacherService {
    private final TeacherRepository teacherRepository;
    private final SchoolRepository schoolRepository;
    private final CourseRepository courseRepository;
    private final TeacherMapper teacherMapper;

    public TeacherServiceImpl(TeacherRepository teacherRepository, SchoolRepository schoolRepository, CourseRepository courseRepository, TeacherMapper teacherMapper) {
        this.teacherRepository = teacherRepository;
        this.schoolRepository = schoolRepository;
        this.courseRepository = courseRepository;
        this.teacherMapper = teacherMapper;
    }

    @Override
    public ResponseData add(TeacherDTO teacherDTO) {
        String schoolId = teacherDTO.getSchoolId();
        schoolRepository.findById(UUID.fromString(schoolId)).orElseThrow(SchoolNotFoundException::new);

        Teacher teacher = teacherMapper.teacherFromTeacherDTO(teacherDTO);
        teacherRepository.save(teacher);
        return new ResponseData(null, "Save success");
    }

    @Override
    public ResponseData get(String id) {
        Teacher teacher = teacherRepository.findById(UUID.fromString(id)).orElseThrow(TeacherNotFoundException::new);
        return new ResponseData(teacher);
    }

    @Override
    public ResponseData delete(String id) {
        teacherRepository.deleteById(UUID.fromString(id));
        return new ResponseData(null, "Delete success");
    }

    @Override
    public ResponseData update(TeacherDTO teacherDTO, String id) {
        UUID uuid = UUID.fromString(id);
        Teacher teacher = teacherRepository.findById(uuid).orElseThrow(TeacherNotFoundException::new);

        UUID schoolUUID = UUID.fromString(teacherDTO.getSchoolId());
        if (!Objects.equals(teacher.getSchoolId(), schoolUUID)) {
            schoolRepository.findById(schoolUUID)
                    .orElseThrow(SchoolNotFoundException::new);
        }

        List<String> positions = teacherDTO.getPositions();
        Set<TeacherPositionEnum> positionEnumSet = positions.stream().map(TeacherPositionEnum::valueOf).collect(Collectors.toSet());
        teacher.setPositions(positionEnumSet);

        teacher.setFullName(teacherDTO.getFullName());
        teacher.setSchoolId(schoolUUID);

        if (StringUtils.hasText(teacherDTO.getEmail())) {
            teacher.setEmail(teacherDTO.getEmail());
        }
        if (StringUtils.hasText(teacherDTO.getOfficePhoneNumber())) {
            teacher.setOfficePhoneNumber(teacher.getOfficePhoneNumber());
        }

        List<String> courseIdsString = teacherDTO.getCourses();
        Set<UUID> courseIds = courseIdsString.stream().map(UUID::fromString).collect(Collectors.toSet());
        Set<Course> courses = courseRepository.findByIdIn(courseIds);
        teacher.setCourses(courses);

        teacherRepository.save(teacher);

        return new ResponseData(null, "Update success");
    }

    @Override
    public ResponseData getBySchoolId(String schoolId) {
        UUID schoolUUID = UUID.fromString(schoolId);
        schoolRepository.findById(schoolUUID)
                .orElseThrow(SchoolNotFoundException::new);

        List<Teacher> teachers = teacherRepository.findAllBySchoolId(schoolUUID);
        return new ResponseData(teachers);
    }
}
