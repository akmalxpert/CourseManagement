package uz.exadel.validations.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uz.exadel.dtos.SchoolDTO;
import uz.exadel.exception.MissingMandatoryFieldException;
import uz.exadel.exception.ValidationException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SchoolValidationServiceImplTest {
    private static final String TEST_SCHOOL_NAME = "test-school-name";
    private static final String TEST_SCHOOL_ADDRESS = "test-school-address";
    private static final String TEST_SCHOOL_PHONE_NUMBER = "123456789";
    private static final String TEST_SCHOOL_POSTAL_CODE = "100000";

    @Mock
    private SchoolValidationServiceImpl schoolValidationService;

    @Test
    @DisplayName("Success during validating creating school")
    void validateCreateSuccess() {
        SchoolDTO schoolDTO = createSchoolDTO();
        doNothing().when(schoolValidationService).validateCreateSchool(schoolDTO);
        schoolValidationService.validateCreateSchool(schoolDTO);
        verify(schoolValidationService, times(1)).validateCreateSchool(schoolDTO);
    }


    @Test
    @DisplayName("Success during validating updating school")
    void validateUpdateSuccess() {
        SchoolDTO schoolDTO = createSchoolDTO();
        doNothing().when(schoolValidationService).validateUpdateSchool(schoolDTO);
        schoolValidationService.validateUpdateSchool(schoolDTO);
        verify(schoolValidationService, times(1)).validateUpdateSchool(schoolDTO);
    }


    @Test()
    @DisplayName("Failure during creating school without name")
    void validateCreateFailure_NoName() {
        SchoolDTO schoolDTO = createSchoolDTO();
        schoolDTO.setName(null);

        try {
            schoolValidationService.validateCreateSchool(schoolDTO);
        } catch (MissingMandatoryFieldException ex) {
            String message = "School name is missing or empty";
            assertEquals(message, ex.getMessage());
        }
    }


    @Test()
    @DisplayName("Failure during updating school without address")
    void validateUpdateFailure_NoAddress() {
        SchoolDTO schoolDTO = createSchoolDTO();
        schoolDTO.setAddress("");

        try {
            schoolValidationService.validateUpdateSchool(schoolDTO);
        } catch (MissingMandatoryFieldException ex) {
            String message = "School address is missing or empty";
            assertEquals(message, ex.getMessage());
        }
    }


    @Test()
    @DisplayName("Failure during creating school with long phone number")
    void validateCreateFailure_LongPhoneNumber() {
        SchoolDTO schoolDTO = createSchoolDTO();
        schoolDTO.setPhoneNumber("123456789897564");

        try {
            schoolValidationService.validateCreateSchool(schoolDTO);
        } catch (ValidationException ex) {
            String message = "Phone number should not be greater than 13 characters";
            assertEquals(message, ex.getMessage());
        }
    }


    @Test()
    @DisplayName("Failure during creating school with long name")
    void validateCreateFailure_LongName() {
        SchoolDTO schoolDTO = createSchoolDTO();
        schoolDTO.setName("qwertyuiopasdfghjklzxcvbnm");

        try {
            schoolValidationService.validateCreateSchool(schoolDTO);
        } catch (ValidationException ex) {
            String message = "Name should not be greater than 25 characters";
            assertEquals(message, ex.getMessage());
        }
    }

    private SchoolDTO createSchoolDTO() {
        SchoolDTO schoolDTO = new SchoolDTO();
        schoolDTO.setName(TEST_SCHOOL_NAME);
        schoolDTO.setAddress(TEST_SCHOOL_ADDRESS);
        schoolDTO.setPhoneNumber(TEST_SCHOOL_PHONE_NUMBER);
        schoolDTO.setPostalCode(TEST_SCHOOL_POSTAL_CODE);

        return schoolDTO;
    }

}
