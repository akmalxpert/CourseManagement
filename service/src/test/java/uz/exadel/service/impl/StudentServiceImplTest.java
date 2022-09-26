package uz.exadel.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import uz.exadel.dtos.ResponseData;
import uz.exadel.dtos.StudentDTO;
import uz.exadel.entity.Course;
import uz.exadel.entity.Group;
import uz.exadel.entity.Student;
import uz.exadel.exception.GroupNotFoundException;
import uz.exadel.exception.StudentNotFoundException;
import uz.exadel.mapper.impl.StudentMapperImpl;
import uz.exadel.repository.GroupRepository;
import uz.exadel.repository.StudentRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {
    private static final String TEST_STUDENT_NAME = "test-student-name";
    private static final String TEST_GROUP_ID = "abbb72fe-e535-11ec-8fea-0242ac120002";
    private static final List<String> TEST_STUDENT_COURSES = Arrays.asList("2047b0ed-5ce4-4db8-975f-be5cc365eb82", "6df1ce6a-2211-4d9b-b612-21cb8be4f6a1");
    private static final String TEST_STUDENT_ID = "bd8e9abd-0a67-440a-9719-21f5cb9d256f";
    private static final UUID TEST_STUDENT_UUID = UUID.fromString(TEST_STUDENT_ID);
    private static final UUID TEST_GROUP_UUID = UUID.fromString(TEST_GROUP_ID);
    private static final Integer TEST_STUDENT_LEVEL = 4;

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentMapperImpl studentMapper;

    private InOrder inOrder;

    @BeforeEach
    void setUp() {
        inOrder = Mockito.inOrder(studentMapper, groupRepository, studentRepository);
    }

    @InjectMocks
    private StudentServiceImpl studentService;

    @Test
    @DisplayName("Success while creating student")
    void createSuccess() {
        //given
        StudentDTO studentDTO = createStudentDTO();
        Student student = createStudent();

        //when
        when(groupRepository.findById(UUID.fromString(TEST_GROUP_ID))).thenReturn(Optional.of(new Group()));
        when(studentMapper.studentFromStudentDTO(studentDTO)).thenReturn(student);
        ResponseData actualResult = studentService.add(studentDTO);

        //then
        inOrder.verify(groupRepository, times(1)).findById(UUID.fromString(TEST_GROUP_ID));
        inOrder.verify(studentRepository, times(1)).save(student);
        assertEquals("Save success", actualResult.getDetail());
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("Failure when creating student - group ID not found")
    void createFailure_GroupIdNotFound() {
        StudentDTO studentDTO = createStudentDTO();

        when(groupRepository.findById(UUID.fromString(TEST_GROUP_ID))).thenReturn(Optional.empty());

        assertThrows(GroupNotFoundException.class, () -> studentService.add(studentDTO));
    }

    @Test
    @DisplayName("Success while getting student by id")
    void getSuccess() {
        Student student = createStudent();

        when(studentRepository.findById(TEST_STUDENT_UUID)).thenReturn(Optional.of(student));

        ResponseData actualResult = studentService.get(TEST_STUDENT_ID);

        inOrder.verify(studentRepository, times(1)).findById(TEST_STUDENT_UUID);
        assertEquals(student, actualResult.getData());
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("Failure while getting student when not found in the database")
    void getFailure_NotFoundInDatabase() {
        when(studentRepository.findById(TEST_STUDENT_UUID)).thenReturn(Optional.empty());
        assertThrows(StudentNotFoundException.class, () -> studentService.get(TEST_STUDENT_ID));
    }

    @Test
    @DisplayName("Success while deleting student")
    void deleteSuccess() {
        Student student = createStudent();
        when(studentRepository.findById(TEST_STUDENT_UUID)).thenReturn(Optional.of(student));

        ResponseData actualResult = studentService.delete(TEST_STUDENT_ID);
        inOrder.verify(studentRepository, times(1)).findById(TEST_STUDENT_UUID);
        assertEquals("Delete success", actualResult.getDetail());
        inOrder.verify(studentRepository).deleteById(TEST_STUDENT_UUID);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("Failure while deleting group when not found in the database")
    void deleteFailure_NotFoundInDatabase() {
        when(studentRepository.findById(TEST_STUDENT_UUID)).thenReturn(Optional.empty());
        assertThrows(StudentNotFoundException.class, () -> studentService.delete(TEST_STUDENT_ID));
    }

    @Test
    @DisplayName("Success while updating student")
    void updateSuccess() {
        StudentDTO studentDTO = createStudentDTO();
        Student student = createStudent();

        when(studentRepository.findById(TEST_STUDENT_UUID)).thenReturn(Optional.of(student));
        when(studentMapper.studentFromStudentDTO(studentDTO)).thenReturn(student);

        ResponseData actualResult = studentService.update(TEST_STUDENT_ID, studentDTO);

        inOrder.verify(studentRepository, times(1)).findById(TEST_STUDENT_UUID);
        inOrder.verify(studentRepository, times(1)).save(student);
        assertEquals("Update success", actualResult.getDetail());
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("Failure while updating student when not found in the database")
    void updateFailure_NotFoundInDatabase() {
        StudentDTO studentDTO = createStudentDTO();
        when(studentRepository.findById(TEST_STUDENT_UUID)).thenReturn(Optional.empty());
        assertThrows(StudentNotFoundException.class, () -> studentService.update(TEST_STUDENT_ID, studentDTO));
    }

    @Test
    @DisplayName("Failure while updating student when group not found in the database")
    void updateFailure_GroupNotFoundInDatabase() {
        StudentDTO studentDTO = createStudentDTO();

        when(studentRepository.findById(TEST_STUDENT_UUID)).thenReturn(Optional.of(new Student()));
        when(groupRepository.findById(TEST_GROUP_UUID)).thenReturn(Optional.empty());

        assertThrows(GroupNotFoundException.class, () -> studentService.update(TEST_STUDENT_ID, studentDTO));
    }

    @Test
    @DisplayName("Success while getting students by group ID")
    void successGetByGroupId() {
        when(groupRepository.findById(TEST_GROUP_UUID)).thenReturn(Optional.of(new Group()));
        when(studentRepository.findAllByGroupId(TEST_GROUP_UUID)).thenReturn(new ArrayList<>());
        assertDoesNotThrow(() -> studentService.getByGroupIdOrLevel(TEST_GROUP_ID, null));
    }

    @Test
    @DisplayName("Success while getting students by level")
    void successGetByLevel() {
        when(studentRepository.findAllByLevel(TEST_STUDENT_LEVEL)).thenReturn(new ArrayList<>());
        assertDoesNotThrow(() -> studentService.getByGroupIdOrLevel(null, TEST_STUDENT_LEVEL));
    }

    private StudentDTO createStudentDTO() {
        return new StudentDTO(
                TEST_STUDENT_NAME,
                TEST_GROUP_ID,
                TEST_STUDENT_COURSES,
                TEST_STUDENT_LEVEL
        );
    }

    private Student createStudent() {
        Set<Course> courseSet = new HashSet<>();
        courseSet.add(new Course());
        return new Student(
                TEST_STUDENT_UUID,
                TEST_STUDENT_NAME,
                TEST_GROUP_UUID,
                courseSet,
                TEST_STUDENT_LEVEL
        );
    }
}
