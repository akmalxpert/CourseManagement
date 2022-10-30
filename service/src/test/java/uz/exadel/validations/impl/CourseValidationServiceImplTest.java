package uz.exadel.validations.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import uz.exadel.dtos.CourseDTO;
import uz.exadel.exception.MissingMandatoryFieldException;
import uz.exadel.exception.ValidationException;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CourseValidationServiceImplTest {
    private static final String TEST_COURSE_ID = "e94d0a42-411a-408a-a4d0-1de0009299ae";
    private static final String TEST_COURSE_CODE = "test-code";
    private static final String TEST_COURSE_NAME = "test-course-name";
    private static final String TEST_COURSE_DESCRIPTION = "test-course-description";
    private static final String TEST_COURSE_SCHOOL_ID = "fdd11fd8-d7a5-4b4c-857b-65a03235c99b";

    @InjectMocks
    private CourseValidationServiceImpl courseValidationService;

    @Test
    @DisplayName("Success while creating course")
    void validateCreateSuccess() {
        CourseDTO courseDTO = createCourseDTO();
        assertDoesNotThrow(() -> courseValidationService.validateCreate(courseDTO));
    }

    @Test
    @DisplayName("Success while updating course")
    void validateUpdateSuccess() {
        CourseDTO courseDTO = createCourseDTO();
        assertDoesNotThrow(() -> courseValidationService.validateUpdate(TEST_COURSE_ID, courseDTO));
    }

    @Test
    @DisplayName("Success while deleting course")
    void validateDeleteSuccess() {
        assertDoesNotThrow(() -> courseValidationService.validateDelete(TEST_COURSE_ID));
    }

    @Test
    @DisplayName("Success while getting course by id")
    void validateGetByIdSuccess() {
        assertDoesNotThrow(() -> courseValidationService.validateGetById(TEST_COURSE_ID));
    }

    @ParameterizedTest
    @MethodSource("blankOrNullStrings")
    @DisplayName("Failure while updating course with empty, whitespace only or null ID")
    void validateUpdateFailure_EmptyOrNullId(String id) {
        CourseDTO courseDTO = createCourseDTO();

        MissingMandatoryFieldException exception = assertThrows(MissingMandatoryFieldException.class,
                () -> courseValidationService.validateUpdate(id, courseDTO));
        assertEquals("Id is missing or empty", exception.getMessage());
    }


    @ParameterizedTest
    @ValueSource(strings = {"test1", "qwertyui", "sample-sample-sample"})
    @DisplayName("Failure while updating course with invalid ID")
    void validateUpdateFailure_InvalidId(String id) {
        CourseDTO courseDTO = createCourseDTO();

        ValidationException exception = assertThrows(ValidationException.class,
                () -> courseValidationService.validateUpdate(id, courseDTO));
        assertEquals("This ID is not valid for the system", exception.getMessage());
    }

    @Test
    @DisplayName("Failure while updating course with invalid ID")
    void validateUpdateFailure_NullSchoolIdAndGroupId() {

        MissingMandatoryFieldException exception = assertThrows(MissingMandatoryFieldException.class,
                () -> courseValidationService.validateGetBySchoolIdOrGroupId(null, null));
        assertEquals("GroupId or SchoolId is missing or empty", exception.getMessage());
    }

    @ParameterizedTest
    @MethodSource("blankOrNullStrings")
    @DisplayName("Failure while updating course with empty, whitespace only or null name")
    void validateUpdateFailure_EmptyOrNullName(String value) {
        CourseDTO courseDTO = createCourseDTO();
        courseDTO.setName(value);

        MissingMandatoryFieldException exception = assertThrows(MissingMandatoryFieldException.class,
                () -> courseValidationService.validateUpdate(TEST_COURSE_ID, courseDTO));
        assertEquals("Course Name is missing or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Failure while creating course with invalid faculty name")
    void validateCreateFailure_InvalidCode() {
        CourseDTO courseDTO = createCourseDTO();
        courseDTO.setCode("123456789987654321");
        ValidationException exception = assertThrows(ValidationException.class,
                () -> courseValidationService.validateCreate(courseDTO));
        assertEquals("Course Code should not be greater than 15 characters", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"test1", "qwertyui", "sample-sample-sample"})
    @DisplayName("Failure while updating course with invalid SchoolId")
    void validateUpdateFailure_InvalidSchoolId(String schoolId) {
        CourseDTO courseDTO = createCourseDTO();
        courseDTO.setSchoolId(schoolId);
        ValidationException exception = assertThrows(ValidationException.class,
                () -> courseValidationService.validateUpdate(TEST_COURSE_ID, courseDTO));
        assertEquals("This ID is not valid for the system", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"test1", "qwertyui", "sample-sample-sample"})
    @DisplayName("Failure while updating course with invalid groupId")
    void validateGetFailure_InvalidGroupId(String groupId) {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> courseValidationService.validateGetBySchoolIdOrGroupId(null, groupId));
        assertEquals("This ID is not valid for the system", exception.getMessage());
    }

    @Test
    @DisplayName("Failure while updating course with both schoolId and groupId")
    void validateGetFailure_BothSchoolIdAndGroupId() {
        String groupId = "7e5bcf4e-b2d7-4b31-b349-0697af7fa96a";
        ValidationException exception = assertThrows(ValidationException.class,
                () -> courseValidationService.validateGetBySchoolIdOrGroupId(TEST_COURSE_SCHOOL_ID, groupId));
        assertEquals("Only groupId or schoolId should be provided", exception.getMessage());
    }

    private CourseDTO createCourseDTO() {
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setName(TEST_COURSE_NAME);
        courseDTO.setCode(TEST_COURSE_CODE);
        courseDTO.setDescription(TEST_COURSE_DESCRIPTION);
        courseDTO.setSchoolId(TEST_COURSE_SCHOOL_ID);
        return courseDTO;
    }

    private static Stream<String> blankOrNullStrings() {
        return Stream.of("", " ", null);
    }
}
