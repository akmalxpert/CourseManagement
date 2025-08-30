package uz.exadel.service.impl;

import org.springframework.stereotype.Service;
import uz.exadel.dtos.ResponseData;
import uz.exadel.dtos.TeacherDTO;
import uz.exadel.entity.Teacher;
import uz.exadel.exception.SchoolNotFoundException;
import uz.exadel.exception.TeacherNotFoundException;
import uz.exadel.mapper.TeacherMapper;
import uz.exadel.repository.SchoolRepository;
import uz.exadel.repository.TeacherRepository;
import uz.exadel.service.TeacherService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Transactional
public class TeacherServiceImpl implements TeacherService {
    private final TeacherRepository teacherRepository;
    private final SchoolRepository schoolRepository;
    private final TeacherMapper teacherMapper;

    public TeacherServiceImpl(TeacherRepository teacherRepository, SchoolRepository schoolRepository, TeacherMapper teacherMapper) {
        this.teacherRepository = teacherRepository;
        this.schoolRepository = schoolRepository;
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
    public ResponseData getAll() {
        List<Teacher> teachers = teacherRepository.findAll();
        return new ResponseData(teachers);
    }

    @Override
    public ResponseData delete(String id) {
        UUID uuid = UUID.fromString(id);
        teacherRepository.findById(uuid).orElseThrow(TeacherNotFoundException::new);

        teacherRepository.deleteById(UUID.fromString(id));
        return new ResponseData(null, "Delete success");
    }

    @Override
    public ResponseData update(TeacherDTO teacherDTO, String id) {
        UUID uuid = UUID.fromString(id);
        Teacher teacher = teacherRepository.findById(uuid).orElseThrow(TeacherNotFoundException::new);

        if (teacherDTO.getSchoolId() != null) {
            UUID schoolUUID = UUID.fromString(teacherDTO.getSchoolId());
            if (!Objects.equals(teacher.getSchoolId(), schoolUUID)) {
                schoolRepository.findById(schoolUUID)
                        .orElseThrow(SchoolNotFoundException::new);
            }
        }

        teacher = teacherMapper.teacherFromTeacherDTOUpdate(teacherDTO, teacher);

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