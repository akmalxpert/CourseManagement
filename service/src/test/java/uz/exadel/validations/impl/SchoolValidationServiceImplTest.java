package uz.exadel.validations.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import uz.exadel.dtos.SchoolDTO;
import uz.exadel.exception.MissingMandatoryFieldException;
import uz.exadel.exception.ValidationException;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SchoolValidationServiceImplTest {
    private static final String TEST_SCHOOL_NAME = "test-school-name";
    private static final String TEST_SCHOOL_ADDRESS = "test-school-address";
    private static final String TEST_SCHOOL_PHONE_NUMBER = "123456789";
    private static final String TEST_SCHOOL_POSTAL_CODE = "100000";
    private static final String TEST_SCHOOL_ID = "abbb72fe-e535-11ec-8fea-0242ac120002";

    @InjectMocks
    private SchoolValidationServiceImpl schoolValidationService;

    @Test
    @DisplayName("Success while validating create school method")
    void validateCreateSuccess() {
        SchoolDTO schoolDTO = createSchoolDTO();
        assertDoesNotThrow(() -> schoolValidationService.validateCreateSchool(schoolDTO));
    }


    @Test
    @DisplayName("Success while validating update school method")
    void validateUpdateSuccess() {
        SchoolDTO schoolDTO = createSchoolDTO();
        assertDoesNotThrow(() -> schoolValidationService.validateUpdateSchool(TEST_SCHOOL_ID, schoolDTO));
    }

    @Test
    @DisplayName("Success while deleting school")
    void validateDeleteSuccess() {
        assertDoesNotThrow(() -> schoolValidationService.validateDeleteSchool(TEST_SCHOOL_ID));
    }

    @Test
    @DisplayName("Success while getting school by id")
    void validateGetByIdSuccess() {
        assertDoesNotThrow(() -> schoolValidationService.validateGetSchoolById(TEST_SCHOOL_ID));
    }

    @ParameterizedTest
    @MethodSource("blankOrNullStrings")
    @DisplayName("Failure while updating school with empty, whitespace only or null ID")
    void validateUpdateFailure_EmptyOrNullId(String id) {
        SchoolDTO schoolDTO = createSchoolDTO();

        MissingMandatoryFieldException exception = assertThrows(MissingMandatoryFieldException.class,
                () -> schoolValidationService.validateUpdateSchool(id, schoolDTO));
        assertEquals("Id is missing or empty", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"test1", "qwertyui", "sample-sample-sample"})
    @DisplayName("Failure while updating school with invalid ID")
    void validateUpdateFailure_InvalidId(String id) {
        SchoolDTO schoolDTO = createSchoolDTO();

        ValidationException exception = assertThrows(ValidationException.class,
                () -> schoolValidationService.validateUpdateSchool(id, schoolDTO));
        assertEquals("This ID is not valid for the system", exception.getMessage());
    }

    @ParameterizedTest
    @MethodSource("blankOrNullStrings")
    @DisplayName("Failure while updating school with empty, whitespace only or null name")
    void validateUpdateFailure_EmptyOrNullName(String value) {
        SchoolDTO schoolDTO = createSchoolDTO();
        schoolDTO.setName(value);

        MissingMandatoryFieldException exception = assertThrows(MissingMandatoryFieldException.class,
                () -> schoolValidationService.validateUpdateSchool(TEST_SCHOOL_ID, schoolDTO));
        assertEquals("School name is missing or empty", exception.getMessage());
    }


    @ParameterizedTest
    @MethodSource("blankOrNullStrings")
    @DisplayName("Failure while updating school with empty, whitespace only or null address")
    void validateUpdateFailure_EmptyOrNullAddress(String address) {
        SchoolDTO schoolDTO = createSchoolDTO();
        schoolDTO.setAddress(address);

        MissingMandatoryFieldException exception = assertThrows(MissingMandatoryFieldException.class,
                () -> schoolValidationService.validateUpdateSchool(TEST_SCHOOL_ID, schoolDTO));
        assertEquals("School address is missing or empty", exception.getMessage());
    }

    @ParameterizedTest
    @MethodSource("blankOrNullStrings")
    @DisplayName("Failure while updating school with empty, whitespace only or null phone number")
    void validateUpdateFailure_EmptyOrNullPhoneNumber(String phoneNumber) {
        SchoolDTO schoolDTO = createSchoolDTO();
        schoolDTO.setPhoneNumber(phoneNumber);

        MissingMandatoryFieldException exception = assertThrows(MissingMandatoryFieldException.class,
                () -> schoolValidationService.validateUpdateSchool(TEST_SCHOOL_ID, schoolDTO));
        assertEquals("School phone number is missing or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Failure while updating school with long phone number")
    void validateUpdateFailure_LongPhoneNumber() {
        SchoolDTO schoolDTO = createSchoolDTO();
        schoolDTO.setPhoneNumber("123456789897564");

        ValidationException exception = assertThrows(ValidationException.class,
                () -> schoolValidationService.validateUpdateSchool(TEST_SCHOOL_ID, schoolDTO));
        assertEquals("Phone number should not be greater than 13 characters", exception.getMessage());
    }


    @Test()
    @DisplayName("Failure while updating school with long name")
    void validateUpdateFailure_LongName() {
        SchoolDTO schoolDTO = createSchoolDTO();
        schoolDTO.setName("qwertyuiopasdfghjklzxcvbnm");

        ValidationException exception = assertThrows(ValidationException.class,
                () -> schoolValidationService.validateUpdateSchool(TEST_SCHOOL_ID, schoolDTO));
        assertEquals("Name should not be greater than 25 characters", exception.getMessage());
    }

    @Test()
    @DisplayName("Failure while creating school with long postal code")
    void validateUpdateFailure_LongPostalCode() {
        SchoolDTO schoolDTO = createSchoolDTO();
        schoolDTO.setPostalCode("qwertyuiopasdfgh");

        ValidationException exception = assertThrows(ValidationException.class,
                () -> schoolValidationService.validateUpdateSchool(TEST_SCHOOL_ID, schoolDTO));
        assertEquals("Postal code should not be greater than 7 characters", exception.getMessage());
    }

    private SchoolDTO createSchoolDTO() {
        return new SchoolDTO(
                TEST_SCHOOL_NAME,
                TEST_SCHOOL_ADDRESS,
                TEST_SCHOOL_PHONE_NUMBER,
                TEST_SCHOOL_POSTAL_CODE
        );
    }

    private static Stream<String> blankOrNullStrings() {
        return Stream.of("", " ", null);
    }


}
