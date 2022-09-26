package uz.exadel.validations.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import uz.exadel.dtos.StudentDTO;
import uz.exadel.exception.MissingMandatoryFieldException;
import uz.exadel.exception.ValidationException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class StudentValidationServiceImplTest {
    private static final String TEST_STUDENT_NAME = "test-student-name";
    private static final String TEST_GROUP_ID = "abbb72fe-e535-11ec-8fea-0242ac120002";
    private static final List<String> TEST_STUDENT_COURSES = Arrays.asList("2047b0ed-5ce4-4db8-975f-be5cc365eb82", "6df1ce6a-2211-4d9b-b612-21cb8be4f6a1");
    private static final String TEST_STUDENT_ID = "bd8e9abd-0a67-440a-9719-21f5cb9d256f";
    private static final Integer TEST_STUDENT_LEVEL = 4;

    @InjectMocks
    private StudentValidationServiceImpl studentValidationService;

    @Test
    @DisplayName("Success while validating create student method")
    void validateCreateSuccess() {
        StudentDTO studentDTO = createStudentDTO();
        assertDoesNotThrow(() -> studentValidationService.validateCreate(studentDTO));
    }


    @Test
    @DisplayName("Success while validating update student method")
    void validateUpdateSuccess() {
        StudentDTO studentDTO = createStudentDTO();
        assertDoesNotThrow(() -> studentValidationService.validateUpdate(TEST_STUDENT_ID, studentDTO));
    }

    @Test
    @DisplayName("Success while deleting student")
    void validateDeleteSuccess() {
        assertDoesNotThrow(() -> studentValidationService.validateDelete(TEST_STUDENT_ID));
    }

    @Test
    @DisplayName("Success while getting student by id")
    void validateGetByIdSuccess() {
        assertDoesNotThrow(() -> studentValidationService.validateGetById(TEST_STUDENT_ID));
    }

    @ParameterizedTest
    @MethodSource("blankOrNullStrings")
    @DisplayName("Failure while updating student with empty, whitespace only or null ID")
    void validateUpdateFailure_EmptyOrNullId(String id) {
        StudentDTO studentDTO = createStudentDTO();

        MissingMandatoryFieldException exception = assertThrows(MissingMandatoryFieldException.class,
                () -> studentValidationService.validateUpdate(id, studentDTO));
        assertEquals("Id is missing or empty", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"test1", "qwertyui", "sample-sample-sample"})
    @DisplayName("Failure while updating student with invalid ID")
    void validateUpdateFailure_InvalidId(String id) {
        StudentDTO studentDTO = createStudentDTO();

        ValidationException exception = assertThrows(ValidationException.class,
                () -> studentValidationService.validateUpdate(id, studentDTO));
        assertEquals("This ID is not valid for the system", exception.getMessage());
    }

    @ParameterizedTest
    @MethodSource("blankOrNullStrings")
    @DisplayName("Failure while updating student with empty, whitespace only or null name")
    void validateUpdateFailure_EmptyOrNullName(String value) {
        StudentDTO studentDTO = createStudentDTO();
        studentDTO.setFullName(value);

        MissingMandatoryFieldException exception = assertThrows(MissingMandatoryFieldException.class,
                () -> studentValidationService.validateUpdate(TEST_STUDENT_ID, studentDTO));
        assertEquals("Full Name is missing or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Failure while updating student with null courses")
    void validateUpdateFailure_NullCourse() {
        StudentDTO studentDTO = createStudentDTO();
        studentDTO.setCourses(null);

        MissingMandatoryFieldException exception = assertThrows(MissingMandatoryFieldException.class,
                () -> studentValidationService.validateUpdate(TEST_STUDENT_ID, studentDTO));
        assertEquals("Courses is missing or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Failure while updating student with null level")
    void validateUpdateFailure_NullLevel() {
        StudentDTO studentDTO = createStudentDTO();
        studentDTO.setLevel(null);

        MissingMandatoryFieldException exception = assertThrows(MissingMandatoryFieldException.class,
                () -> studentValidationService.validateUpdate(TEST_STUDENT_ID, studentDTO));
        assertEquals("Student Level is missing or empty", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, 17})
    @DisplayName("Failure while updating student with invalid level")
    void validateUpdateFailure_InvalidLevel(Integer level) {
        StudentDTO studentDTO = createStudentDTO();
        studentDTO.setLevel(level);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> studentValidationService.validateUpdate(TEST_STUDENT_ID, studentDTO));
        assertEquals("Invalid level (1-11)", exception.getMessage());
    }

    @Test
    @DisplayName("Failure while getting student with both groupId and level")
    void validateGetFailure_GroupIdAndLevel() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> studentValidationService.validateGetByGroupIdOrLevel(TEST_GROUP_ID, TEST_STUDENT_LEVEL));
        assertEquals("Only groupId or level should be provided", exception.getMessage());
    }

    @Test
    @DisplayName("Failure while creating student with long full name")
    void validateCreateFailure_LongFullName() {
        StudentDTO studentDTO = createStudentDTO();
        studentDTO.setFullName("12345678912345678912345678");

        ValidationException exception = assertThrows(ValidationException.class,
                () -> studentValidationService.validateCreate(studentDTO));
        assertEquals("Full Name should not be greater than 25 characters", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"test1", "qwertyui", "sample-sample-sample"})
    @DisplayName("Failure while creating student with invalid group ID")
    void validateCreateFailure_InvalidGroupId(String groupId) {
        StudentDTO studentDTO = createStudentDTO();
        studentDTO.setGroupId(groupId);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> studentValidationService.validateCreate(studentDTO));
        assertEquals("This ID is not valid for the system", exception.getMessage());
    }


    private StudentDTO createStudentDTO() {
        return new StudentDTO(
                TEST_STUDENT_NAME,
                TEST_GROUP_ID,
                TEST_STUDENT_COURSES,
                TEST_STUDENT_LEVEL
        );
    }

    private static Stream<String> blankOrNullStrings() {
        return Stream.of("", " ", null);
    }
}
