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
import uz.exadel.dtos.CourseDTO;
import uz.exadel.dtos.ResponseData;
import uz.exadel.entity.Course;
import uz.exadel.entity.Group;
import uz.exadel.entity.School;
import uz.exadel.exception.CourseNotFoundException;
import uz.exadel.exception.SchoolNotFoundException;
import uz.exadel.mapper.CourseMapper;
import uz.exadel.repository.CourseRepository;
import uz.exadel.repository.GroupRepository;
import uz.exadel.repository.SchoolRepository;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {
    private static final String TEST_COURSE_ID = "e94d0a42-411a-408a-a4d0-1de0009299ae";
    private static final String TEST_COURSE_CODE = "test-code";
    private static final String TEST_COURSE_NAME = "test-course-name";
    private static final String TEST_COURSE_DESCRIPTION = "test-course-description";
    private static final String TEST_COURSE_SCHOOL_ID = "fdd11fd8-d7a5-4b4c-857b-65a03235c99b";
    private static final UUID TEST_COURSE_UUID = UUID.fromString(TEST_COURSE_ID);
    private static final UUID TEST_COURSE_SCHOOL_UUID = UUID.fromString(TEST_COURSE_SCHOOL_ID);

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private SchoolRepository schoolRepository;

    @Mock
    private GroupRepository groupRepository;

    private InOrder inOrder;

    @BeforeEach
    void setUp() {
        inOrder = Mockito.inOrder(courseRepository, schoolRepository, groupRepository);
    }

    @InjectMocks
    private CourseServiceImpl courseService;

    @Test
    @DisplayName("Success while creating course")
    void createSuccess() {
        //given
        CourseDTO courseDTO = createCourseDTO();

        //when
        when(schoolRepository.findById(TEST_COURSE_SCHOOL_UUID)).thenReturn(Optional.of(new School()));
        ResponseData actualResult = courseService.add(courseDTO);

        //then
        assertEquals("Save success", actualResult.getDetail());
    }

    @Test
    @DisplayName("Failure when creating course - school ID not found")
    void createFailure_SchoolIdNotFound() {
        CourseDTO courseDTO = createCourseDTO();

        when(schoolRepository.findById(TEST_COURSE_SCHOOL_UUID)).thenReturn(Optional.empty());

        assertThrows(SchoolNotFoundException.class, () -> courseService.add(courseDTO));
    }

    @Test
    @DisplayName("Success while getting course by id")
    void getSuccess() {
        Course course = createCourse();
        course.setId(TEST_COURSE_UUID);
        when(courseRepository.findById(TEST_COURSE_UUID)).thenReturn(Optional.of(course));

        ResponseData actualResult = courseService.get(TEST_COURSE_ID);

        inOrder.verify(courseRepository, times(1)).findById(TEST_COURSE_UUID);
        assertEquals(course, actualResult.getData());
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("Failure while getting course when not found in the database")
    void getFailure_NotFoundInDatabase() {
        when(courseRepository.findById(TEST_COURSE_UUID)).thenReturn(Optional.empty());
        assertThrows(CourseNotFoundException.class, () -> courseService.get(TEST_COURSE_ID));
    }

    @Test
    @DisplayName("Success while deleting course")
    void deleteSuccess() {
        Course course = createCourse();
        course.setId(TEST_COURSE_UUID);
        when(courseRepository.findById(TEST_COURSE_UUID)).thenReturn(Optional.of(course));

        ResponseData actualResult = courseService.delete(TEST_COURSE_ID);

        inOrder.verify(courseRepository, times(1)).findById(TEST_COURSE_UUID);
        assertEquals("Delete success", actualResult.getDetail());
        inOrder.verify(courseRepository).deleteById(TEST_COURSE_UUID);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("Failure while deleting course when not found in the database")
    void deleteFailure_NotFoundInDatabase() {
        when(courseRepository.findById(TEST_COURSE_UUID)).thenReturn(Optional.empty());
        assertThrows(CourseNotFoundException.class, () -> courseService.delete(TEST_COURSE_ID));
    }

    @Test
    @DisplayName("Success while updating course")
    void updateSuccess() {
        CourseDTO courseDTO = createCourseDTO();
        Course course = createCourse();

        when(courseRepository.findById(TEST_COURSE_UUID)).thenReturn(Optional.of(course));

        ResponseData actualResult = courseService.update(TEST_COURSE_ID, courseDTO);

        assertEquals("Update success", actualResult.getDetail());
    }

    @Test
    @DisplayName("Failure while updating course when not found in the database")
    void updateFailure_NotFoundInDatabase() {
        CourseDTO courseDTO = createCourseDTO();
        when(courseRepository.findById(TEST_COURSE_UUID)).thenReturn(Optional.empty());
        assertThrows(CourseNotFoundException.class, () -> courseService.update(TEST_COURSE_ID, courseDTO));
    }

    @Test
    @DisplayName("Failure while updating course when school not found in the database")
    void updateFailure_SchoolNotFoundInDatabase() {
        CourseDTO courseDTO = createCourseDTO();
        String schoolID = "ce6b8b16-84e9-4250-97ef-465ff6de0575";
        courseDTO.setSchoolId(schoolID);
        Course course = createCourse();

        when(courseRepository.findById(TEST_COURSE_UUID)).thenReturn(Optional.of(course));
        when(schoolRepository.findById(UUID.fromString(schoolID))).thenReturn(Optional.empty());
        assertThrows(SchoolNotFoundException.class, () -> courseService.update(TEST_COURSE_ID, courseDTO));
    }

    @Test
    @DisplayName("Success while getting schools by school ID")
    void successGetBySchoolId() {
        when(schoolRepository.findById(TEST_COURSE_SCHOOL_UUID)).thenReturn(Optional.of(new School()));
        when(courseRepository.findAllBySchoolId(TEST_COURSE_SCHOOL_UUID)).thenReturn(new ArrayList<>());
        assertDoesNotThrow(() -> courseService.getBySchoolIdOrGroupId(TEST_COURSE_SCHOOL_ID, null));
    }

    @Test
    @DisplayName("Success while getting schools by group ID")
    void successGetByGroupId() {
        Group group = new Group();
        group.setSchoolId(TEST_COURSE_SCHOOL_UUID);
        when(groupRepository.findById(UUID.fromString("6f3bb9b3-cb19-4b80-bdf5-7e56cd055412"))).thenReturn(Optional.of(group));

        when(schoolRepository.findById(TEST_COURSE_SCHOOL_UUID)).thenReturn(Optional.of(new School()));
        when(courseRepository.findAllBySchoolId(TEST_COURSE_SCHOOL_UUID)).thenReturn(new ArrayList<>());
        assertDoesNotThrow(() -> courseService.getBySchoolIdOrGroupId(null, "6f3bb9b3-cb19-4b80-bdf5-7e56cd055412"));
    }

    private CourseDTO createCourseDTO() {
        return new CourseDTO(
                TEST_COURSE_CODE,
                TEST_COURSE_NAME,
                TEST_COURSE_DESCRIPTION,
                TEST_COURSE_SCHOOL_ID
        );
    }

    private Course createCourse() {
        CourseDTO courseDTO = createCourseDTO();
        return CourseMapper.INSTANCE.courseToCourseDTO(courseDTO);
    }

}
