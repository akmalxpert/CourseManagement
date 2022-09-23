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
import uz.exadel.dtos.TeacherDTO;
import uz.exadel.entity.Course;
import uz.exadel.entity.School;
import uz.exadel.entity.Teacher;
import uz.exadel.enums.TeacherPositionEnum;
import uz.exadel.exception.SchoolNotFoundException;
import uz.exadel.exception.TeacherNotFoundException;
import uz.exadel.mapper.impl.TeacherMapperImpl;
import uz.exadel.repository.SchoolRepository;
import uz.exadel.repository.TeacherRepository;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceImplTest {
    private static final String TEST_TEACHER_NAME = "test-teacher-name";
    private static final List<String> TEST_TEACHER_POSITIONS = Collections.singletonList("LECTURER");
    private static final String TEST_TEACHER_EMAIL = "test@email.com";
    private static final String TEST_TEACHER_PHONE_NUMBER = "123456787";
    private static final String TEST_SCHOOL_ID = "abbb72fe-e535-11ec-8fea-0242ac120002";
    private static final List<String> TEST_TEACHER_COURSES = Arrays.asList("2047b0ed-5ce4-4db8-975f-be5cc365eb82", "6df1ce6a-2211-4d9b-b612-21cb8be4f6a1");
    private static final String TEST_TEACHER_ID = "bd8e9abd-0a67-440a-9719-21f5cb9d256f";
    private static final UUID TEST_TEACHER_UUID = UUID.fromString(TEST_TEACHER_ID);
    private static final UUID TEST_SCHOOL_UUID = UUID.fromString(TEST_SCHOOL_ID);

    @Mock
    private SchoolRepository schoolRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private TeacherMapperImpl teacherMapper;

    private InOrder inOrder;

    @BeforeEach
    void setUp() {
        inOrder = Mockito.inOrder(teacherMapper, schoolRepository, teacherRepository);
    }

    @InjectMocks
    private TeacherServiceImpl teacherService;

    @Test
    @DisplayName("Success while creating teacher")
    void createSuccess() {
        //given
        TeacherDTO teacherDTO = createTeacherDTO();
        Teacher teacher = createTeacher();

        //when
        when(schoolRepository.findById(UUID.fromString(TEST_SCHOOL_ID))).thenReturn(Optional.of(new School()));
        when(teacherMapper.teacherFromTeacherDTO(teacherDTO)).thenReturn(teacher);
        ResponseData actualResult = teacherService.add(teacherDTO);

        //then
        inOrder.verify(schoolRepository, times(1)).findById(UUID.fromString(TEST_SCHOOL_ID));
        inOrder.verify(teacherRepository, times(1)).save(teacher);
        assertEquals("Save success", actualResult.getDetail());
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("Failure when creating teacher - school ID not found")
    void createFailure_SchoolIdNotFound() {
        TeacherDTO teacherDTO = createTeacherDTO();

        when(schoolRepository.findById(UUID.fromString(TEST_SCHOOL_ID))).thenReturn(Optional.empty());

        assertThrows(SchoolNotFoundException.class, () -> teacherService.add(teacherDTO));
    }

    @Test
    @DisplayName("Success while getting teacher by id")
    void getSuccess() {
        Teacher teacher = createTeacher();

        when(teacherRepository.findById(TEST_TEACHER_UUID)).thenReturn(Optional.of(teacher));

        ResponseData actualResult = teacherService.get(TEST_TEACHER_ID);

        inOrder.verify(teacherRepository, times(1)).findById(TEST_TEACHER_UUID);
        assertEquals(teacher, actualResult.getData());
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("Failure while getting teacher when not found in the database")
    void getFailure_NotFoundInDatabase() {
        when(teacherRepository.findById(TEST_TEACHER_UUID)).thenReturn(Optional.empty());
        assertThrows(TeacherNotFoundException.class, () -> teacherService.get(TEST_TEACHER_ID));
    }

    @Test
    @DisplayName("Success while deleting teacher")
    void deleteSuccess() {
        Teacher teacher = createTeacher();
        when(teacherRepository.findById(TEST_TEACHER_UUID)).thenReturn(Optional.of(teacher));

        ResponseData actualResult = teacherService.delete(TEST_TEACHER_ID);
        inOrder.verify(teacherRepository, times(1)).findById(TEST_TEACHER_UUID);
        assertEquals("Delete success", actualResult.getDetail());
        inOrder.verify(teacherRepository).deleteById(TEST_TEACHER_UUID);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("Failure while deleting group when not found in the database")
    void deleteFailure_NotFoundInDatabase() {
        when(teacherRepository.findById(TEST_TEACHER_UUID)).thenReturn(Optional.empty());
        assertThrows(TeacherNotFoundException.class, () -> teacherService.delete(TEST_TEACHER_ID));
    }

    @Test
    @DisplayName("Success while updating teacher")
    void updateSuccess() {
        TeacherDTO teacherDTO = createTeacherDTO();
        Teacher teacher = createTeacher();

        when(teacherRepository.findById(TEST_TEACHER_UUID)).thenReturn(Optional.of(teacher));
        when(teacherMapper.teacherFromTeacherDTO(teacherDTO)).thenReturn(teacher);

        ResponseData actualResult = teacherService.update(teacherDTO, TEST_TEACHER_ID);

        inOrder.verify(teacherRepository, times(1)).findById(TEST_TEACHER_UUID);
        inOrder.verify(teacherRepository, times(1)).save(teacher);
        assertEquals("Update success", actualResult.getDetail());
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("Failure while updating teacher when not found in the database")
    void updateFailure_NotFoundInDatabase() {
        TeacherDTO teacherDTO = createTeacherDTO();
        when(teacherRepository.findById(TEST_TEACHER_UUID)).thenReturn(Optional.empty());
        assertThrows(TeacherNotFoundException.class, () -> teacherService.update(teacherDTO, TEST_TEACHER_ID));
    }

    @Test
    @DisplayName("Failure while updating teacher when school not found in the database")
    void updateFailure_SchoolNotFoundInDatabase() {
        TeacherDTO teacherDTO = createTeacherDTO();

        when(teacherRepository.findById(TEST_TEACHER_UUID)).thenReturn(Optional.of(new Teacher()));
        when(schoolRepository.findById(TEST_SCHOOL_UUID)).thenReturn(Optional.empty());

        assertThrows(SchoolNotFoundException.class, () -> teacherService.update(teacherDTO, TEST_TEACHER_ID));
    }

    @Test
    @DisplayName("Success while getting teachers by school ID")
    void successGetBySchoolId() {
        when(schoolRepository.findById(TEST_SCHOOL_UUID)).thenReturn(Optional.of(new School()));
        when(teacherRepository.findAllBySchoolId(TEST_SCHOOL_UUID)).thenReturn(new ArrayList<>());
        assertDoesNotThrow(() -> teacherService.getBySchoolId(TEST_SCHOOL_ID));
    }

    private TeacherDTO createTeacherDTO() {
        return new TeacherDTO(
                TEST_TEACHER_NAME,
                TEST_TEACHER_POSITIONS,
                TEST_TEACHER_EMAIL,
                TEST_TEACHER_PHONE_NUMBER,
                TEST_TEACHER_COURSES,
                TEST_SCHOOL_ID
        );
    }

    private Teacher createTeacher() {
        Set<TeacherPositionEnum> positionSet = TEST_TEACHER_POSITIONS.stream().map(TeacherPositionEnum::valueOf).collect(Collectors.toSet());
        Set<Course> courseSet = new HashSet<>();
        courseSet.add(new Course());
        return new Teacher(
                TEST_TEACHER_UUID,
                TEST_TEACHER_NAME,
                positionSet,
                TEST_TEACHER_EMAIL,
                TEST_TEACHER_PHONE_NUMBER,
                courseSet,
                TEST_SCHOOL_UUID
        );
    }
}
