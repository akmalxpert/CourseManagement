package uz.exadel.service.impl;

import org.springframework.stereotype.Service;
import uz.exadel.dtos.ResponseData;
import uz.exadel.dtos.StudentDTO;
import uz.exadel.entity.Student;
import uz.exadel.exception.GroupNotFoundException;
import uz.exadel.exception.MissingMandatoryFieldException;
import uz.exadel.exception.StudentNotFoundException;
import uz.exadel.mapper.StudentMapper;
import uz.exadel.repository.GroupRepository;
import uz.exadel.repository.StudentRepository;
import uz.exadel.service.StudentService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;
    private final StudentMapper studentMapper;

    public StudentServiceImpl(StudentRepository studentRepository, GroupRepository groupRepository, StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.groupRepository = groupRepository;
        this.studentMapper = studentMapper;
    }

    @Override
    public ResponseData add(StudentDTO studentDTO) {
        String groupId = studentDTO.getGroupId();
        groupRepository.findById(UUID.fromString(groupId)).orElseThrow(GroupNotFoundException::new);

        Student student = studentMapper.studentFromStudentDTO(studentDTO);
        studentRepository.save(student);
        return new ResponseData(null, "Save success");
    }

    @Override
    public ResponseData get(String id) {
        Student student = studentRepository.findById(UUID.fromString(id)).orElseThrow(StudentNotFoundException::new);
        return new ResponseData(student);
    }

    @Override
    public ResponseData delete(String id) {
        UUID uuid = UUID.fromString(id);
        studentRepository.findById(uuid).orElseThrow(StudentNotFoundException::new);

        studentRepository.deleteById(UUID.fromString(id));
        return new ResponseData(null, "Delete success");
    }

    @Override
    public ResponseData update(String id, StudentDTO studentDTO) {
        UUID uuid = UUID.fromString(id);
        Student student = studentRepository.findById(uuid).orElseThrow(StudentNotFoundException::new);

        UUID groupUUID = UUID.fromString(studentDTO.getGroupId());
        if (!Objects.equals(student.getGroupId(), groupUUID)) {
            groupRepository.findById(groupUUID)
                    .orElseThrow(GroupNotFoundException::new);
        }

        student = studentMapper.studentFromStudentDTOUpdate(studentDTO, student);

        studentRepository.save(student);

        return new ResponseData(null, "Update success");
    }

    @Override
    public ResponseData getByGroupIdOrLevel(String groupId, Integer level) {
        if (groupId != null && level == null) {
            UUID groupUUID = UUID.fromString(groupId);
            groupRepository.findById(groupUUID)
                    .orElseThrow(GroupNotFoundException::new);

            List<Student> students = studentRepository.findAllByGroupId(groupUUID);
            return new ResponseData(students);
        } else if (groupId == null && level != null) {
            List<Student> students = studentRepository.findAllByLevel(level);
            return new ResponseData(students);
        } else {
            throw new MissingMandatoryFieldException("GroupId or Level");
        }
    }
}