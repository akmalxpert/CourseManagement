package uz.exadel.validations.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import uz.exadel.dtos.GroupDTO;
import uz.exadel.exception.MissingMandatoryFieldException;
import uz.exadel.exception.ValidationException;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GroupValidationServiceImplTest {
    private static final String TEST_GROUP_ID = "e94d0a42-411a-408a-a4d0-1de0009299ae";
    private static final String TEST_GROUP_NAME = "test-group-name";
    private static final Integer TEST_GROUP_LEVEL = 4;
    private static final String TEST_GROUP_FACULTY = "BUSINESS_INFORMATION_SYSTEMS";
    private static final String TEST_GROUP_SCHOOL_ID = "fdd11fd8-d7a5-4b4c-857b-65a03235c99b";

    @InjectMocks
    private GroupValidationServiceImpl groupValidationService;

    @Test
    @DisplayName("Success while creating group")
    void validateCreateSuccess() {
        GroupDTO groupDTO = createGroupDTO();
        assertDoesNotThrow(() -> groupValidationService.validateCreateGroup(groupDTO));
    }

    @Test
    @DisplayName("Success while updating group")
    void validateUpdateSuccess() {
        GroupDTO groupDTO = createGroupDTO();
        assertDoesNotThrow(() -> groupValidationService.validateUpdateGroup(TEST_GROUP_ID, groupDTO));
    }

    @Test
    @DisplayName("Success while deleting group")
    void validateDeleteSuccess() {
        assertDoesNotThrow(() -> groupValidationService.validateDeleteGroup(TEST_GROUP_ID));
    }

    @Test
    @DisplayName("Success while getting group by id")
    void validateGetByIdSuccess() {
        assertDoesNotThrow(() -> groupValidationService.validateGetGroupById(TEST_GROUP_ID));
    }

    @ParameterizedTest
    @MethodSource("blankOrNullStrings")
    @DisplayName("Failure while updating group with empty, whitespace only or null ID")
    void validateUpdateFailure_EmptyOrNullId(String id) {
        GroupDTO groupDTO = createGroupDTO();

        MissingMandatoryFieldException exception = assertThrows(MissingMandatoryFieldException.class,
                () -> groupValidationService.validateUpdateGroup(id, groupDTO));
        assertEquals("Id is missing or empty", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    @DisplayName("Failure while updating group with empty, whitespace only or null faculty")
    void validateGetBySchoolIdAndFacultyFailure_EmptyOrNullFaculty(String faculty) {
        MissingMandatoryFieldException exception = assertThrows(MissingMandatoryFieldException.class,
                () -> groupValidationService.validateGetGroupBySchoolIdAndFaculty(TEST_GROUP_SCHOOL_ID, faculty));
        assertEquals("Faculty is missing or empty", exception.getMessage());
    }


    @ParameterizedTest
    @ValueSource(strings = {"test1", "qwertyui", "sample-sample-sample"})
    @DisplayName("Failure while updating group with invalid ID")
    void validateUpdateFailure_InvalidId(String id) {
        GroupDTO groupDTO = createGroupDTO();

        ValidationException exception = assertThrows(ValidationException.class,
                () -> groupValidationService.validateUpdateGroup(id, groupDTO));
        assertEquals("This ID is not valid for the system", exception.getMessage());
    }

    @ParameterizedTest
    @MethodSource("blankOrNullStrings")
    @DisplayName("Failure while updating group with empty, whitespace only or null name")
    void validateUpdateFailure_EmptyOrNullName(String value) {
        GroupDTO groupDTO = createGroupDTO();
        groupDTO.setName(value);

        MissingMandatoryFieldException exception = assertThrows(MissingMandatoryFieldException.class,
                () -> groupValidationService.validateUpdateGroup(TEST_GROUP_ID, groupDTO));
        assertEquals("Group Name is missing or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Failure while updating group with empty, whitespace only or null level")
    void validateUpdateFailure_NullLevel() {
        GroupDTO groupDTO = createGroupDTO();
        groupDTO.setLevel(null);

        MissingMandatoryFieldException exception = assertThrows(MissingMandatoryFieldException.class,
                () -> groupValidationService.validateUpdateGroup(TEST_GROUP_ID, groupDTO));
        assertEquals("Group Level is missing or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Failure while updating group with empty, whitespace only or null faculty")
    void validateUpdateFailure_NullFaculty() {
        GroupDTO groupDTO = createGroupDTO();
        groupDTO.setFaculty(null);

        MissingMandatoryFieldException exception = assertThrows(MissingMandatoryFieldException.class,
                () -> groupValidationService.validateUpdateGroup(TEST_GROUP_ID, groupDTO));
        assertEquals("Faculty is missing or empty", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 12, 25, -4})
    @DisplayName("Failure while updating group with invalid level")
    void validateUpdateFailure_InvalidLevel(Integer value) {
        GroupDTO groupDTO = createGroupDTO();
        groupDTO.setLevel(value);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> groupValidationService.validateUpdateGroup(TEST_GROUP_ID, groupDTO));
        assertEquals("Invalid group level (1-11)", exception.getMessage());
    }

    @Test
    @DisplayName("Failure while creating group with invalid faculty name")
    void validateCreateFailure_InvalidFaculty() {
        GroupDTO groupDTO = createGroupDTO();
        groupDTO.setFaculty("TEST FACULTY");
        ValidationException exception = assertThrows(ValidationException.class,
                () -> groupValidationService.validateCreateGroup(groupDTO));
        assertEquals("This Faculty is not valid for the system", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"test1", "qwertyui", "sample-sample-sample"})
    @DisplayName("Failure while updating group with invalid SchoolId")
    void validateUpdateFailure_InvalidSchoolId(String schoolId) {
        GroupDTO groupDTO = createGroupDTO();
        groupDTO.setSchoolId(schoolId);
        ValidationException exception = assertThrows(ValidationException.class,
                () -> groupValidationService.validateUpdateGroup(TEST_GROUP_ID, groupDTO));
        assertEquals("This ID is not valid for the system", exception.getMessage());
    }

    private GroupDTO createGroupDTO() {
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setName(TEST_GROUP_NAME);
        groupDTO.setLevel(TEST_GROUP_LEVEL);
        groupDTO.setFaculty(TEST_GROUP_FACULTY);
        groupDTO.setSchoolId(TEST_GROUP_SCHOOL_ID);
        return groupDTO;
    }

    private static Stream<String> blankOrNullStrings() {
        return Stream.of("", " ", null);
    }
}
