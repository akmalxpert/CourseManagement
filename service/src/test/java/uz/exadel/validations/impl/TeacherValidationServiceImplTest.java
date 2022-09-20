package uz.exadel.validations.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import uz.exadel.dtos.TeacherDTO;
import uz.exadel.exception.MissingMandatoryFieldException;
import uz.exadel.exception.ValidationException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TeacherValidationServiceImplTest {
    private static final String TEST_TEACHER_NAME = "test-teacher-name";
    private static final List<String> TEST_TEACHER_POSITIONS = Collections.singletonList("LECTURER");
    private static final String TEST_TEACHER_EMAIL = "test@email.com";
    private static final String TEST_TEACHER_PHONE_NUMBER = "123456787";
    private static final String TEST_SCHOOL_ID = "abbb72fe-e535-11ec-8fea-0242ac120002";
    private static final List<String> TEST_TEACHER_COURSES = Arrays.asList("2047b0ed-5ce4-4db8-975f-be5cc365eb82", "6df1ce6a-2211-4d9b-b612-21cb8be4f6a1");
    private static final String TEST_TEACHER_ID = "9fd035e7-9de6-404f-87f2-eeab42f9b352";

    @InjectMocks
    private TeacherValidationServiceImpl teacherValidationService;

    @Test
    @DisplayName("Success while validating create teacher method")
    void validateCreateSuccess() {
        TeacherDTO teacherDTO = createTeacherDTO();
        assertDoesNotThrow(() -> teacherValidationService.validateCreate(teacherDTO));
    }


    @Test
    @DisplayName("Success while validating update teacher method")
    void validateUpdateSuccess() {
        TeacherDTO teacherDTO = createTeacherDTO();
        assertDoesNotThrow(() -> teacherValidationService.validateUpdate(teacherDTO, TEST_TEACHER_ID));
    }

    @Test
    @DisplayName("Success while deleting teacher")
    void validateDeleteSuccess() {
        assertDoesNotThrow(() -> teacherValidationService.validateDelete(TEST_TEACHER_ID));
    }

    @Test
    @DisplayName("Success while getting teacher by id")
    void validateGetByIdSuccess() {
        assertDoesNotThrow(() -> teacherValidationService.validateGetById(TEST_TEACHER_ID));
    }

    @ParameterizedTest
    @MethodSource("blankOrNullStrings")
    @DisplayName("Failure while updating teacher with empty, whitespace only or null ID")
    void validateUpdateFailure_EmptyOrNullId(String id) {
        TeacherDTO teacherDTO = createTeacherDTO();

        MissingMandatoryFieldException exception = assertThrows(MissingMandatoryFieldException.class,
                () -> teacherValidationService.validateUpdate(teacherDTO, id));
        assertEquals("Id is missing or empty", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"test1", "qwertyui", "sample-sample-sample"})
    @DisplayName("Failure while updating teacher with invalid ID")
    void validateUpdateFailure_InvalidId(String id) {
        TeacherDTO teacherDTO = createTeacherDTO();

        ValidationException exception = assertThrows(ValidationException.class,
                () -> teacherValidationService.validateUpdate(teacherDTO, id));
        assertEquals("This ID is not valid for the system", exception.getMessage());
    }

    @ParameterizedTest
    @MethodSource("blankOrNullStrings")
    @DisplayName("Failure while updating teacher with empty, whitespace only or null name")
    void validateUpdateFailure_EmptyOrNullName(String value) {
        TeacherDTO teacherDTO = createTeacherDTO();
        teacherDTO.setFullName(value);

        MissingMandatoryFieldException exception = assertThrows(MissingMandatoryFieldException.class,
                () -> teacherValidationService.validateUpdate(teacherDTO, TEST_TEACHER_ID));
        assertEquals("Full Name is missing or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Failure while updating teacher with null courses")
    void validateUpdateFailure_NullCourse() {
        TeacherDTO teacherDTO = createTeacherDTO();
        teacherDTO.setCourses(null);

        MissingMandatoryFieldException exception = assertThrows(MissingMandatoryFieldException.class,
                () -> teacherValidationService.validateUpdate(teacherDTO, TEST_TEACHER_ID));
        assertEquals("Courses is missing or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Failure while updating teacher with null positions")
    void validateUpdateFailure_NullPositions() {
        TeacherDTO teacherDTO = createTeacherDTO();
        teacherDTO.setPositions(null);

        MissingMandatoryFieldException exception = assertThrows(MissingMandatoryFieldException.class,
                () -> teacherValidationService.validateUpdate(teacherDTO, TEST_TEACHER_ID));
        assertEquals("Teacher Positions is missing or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Failure while creating group with invalid position")
    void validateCreateFailure_InvalidPositions() {
        TeacherDTO teacherDTO = createTeacherDTO();
        teacherDTO.setPositions(Collections.singletonList("lecturer"));

        ValidationException exception = assertThrows(ValidationException.class,
                () -> teacherValidationService.validateCreate(teacherDTO));
        assertEquals("Position(s) is not valid for the system", exception.getMessage());
    }

    @Test
    @DisplayName("Failure while creating teacher with long full name")
    void validateCreateFailure_LongFullName() {
        TeacherDTO teacherDTO = createTeacherDTO();
        teacherDTO.setFullName("12345678912345678912345678");

        ValidationException exception = assertThrows(ValidationException.class,
                () -> teacherValidationService.validateCreate(teacherDTO));
        assertEquals("Full Name should not be greater than 25 characters", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"test1", "qwertyui", "sample-sample-sample"})
    @DisplayName("Failure while creating teacher with invalid school ID")
    void validateCreateFailure_InvalidSchoolId(String schoolId) {
        TeacherDTO teacherDTO = createTeacherDTO();
        teacherDTO.setSchoolId(schoolId);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> teacherValidationService.validateCreate(teacherDTO));
        assertEquals("This ID is not valid for the system", exception.getMessage());
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

    private static Stream<String> blankOrNullStrings() {
        return Stream.of("", " ", null);
    }
}
