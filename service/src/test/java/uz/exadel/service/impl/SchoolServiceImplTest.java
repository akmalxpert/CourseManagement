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
import uz.exadel.dtos.SchoolDTO;
import uz.exadel.entity.School;
import uz.exadel.exception.MissingMandatoryFieldException;
import uz.exadel.exception.SchoolNotFoundException;
import uz.exadel.repository.SchoolRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SchoolServiceImplTest {

    private static final String TEST_SCHOOL_NAME = "test-school-name";
    private static final String TEST_SCHOOL_ADDRESS = "test-school-address";
    private static final String TEST_SCHOOL_PHONE_NUMBER = "123456789";
    private static final String TEST_SCHOOL_POSTAL_CODE = "100000";
    private static final String TEST_SCHOOL_ID = "abbb72fe-e535-11ec-8fea-0242ac120002";
    private static final UUID TEST_SCHOOL_UUID = UUID.fromString(TEST_SCHOOL_ID);

    @Mock
    private SchoolRepository schoolRepository;

    private InOrder inOrder;

    @BeforeEach
    void setUp() {
        inOrder = Mockito.inOrder(schoolRepository);
    }

    @InjectMocks
    private SchoolServiceImpl schoolService;

    @Test
    @DisplayName("Success during create School")
    void createSuccess() {
        //given
        SchoolDTO schoolDTO = createSchoolDTO();
        School inputSchool = createSchool();

        School outputSchool = new School();
        outputSchool.setId(UUID.randomUUID());

        //when
        String actualResult = schoolService.add(schoolDTO);

        //then
        inOrder.verify(schoolRepository, times(1)).save(inputSchool);
        assertEquals("Save success", actualResult);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("Failure during create School with long name")
    void createFailure_LongerName() {
        //given
        SchoolDTO schoolDTO = createSchoolDTO();
        schoolDTO.setName("blablablablablablablablablablablablablablabla");

        School inputSchool = createSchool();
        inputSchool.setName("blablablablablablablablablablablablablablabla");

        when(schoolRepository.save(inputSchool)).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> schoolService.add(schoolDTO));
    }

    @Test
    @DisplayName("Failure during create School with no phone number")
    void createFailure_NoPhoneNumber() {
        //given
        SchoolDTO schoolDTO = createSchoolDTO();
        schoolDTO.setPhoneNumber(null);

        School inputSchool = createSchool();
        inputSchool.setPhoneNumber(null);

        when(schoolRepository.save(inputSchool)).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> schoolService.add(schoolDTO));
    }

    @Test
    @DisplayName("Success during getting school by id")
    void getSuccess() {
        School school = createSchool();
        school.setId(TEST_SCHOOL_UUID);
        when(schoolRepository.findById(TEST_SCHOOL_UUID)).thenReturn(Optional.of(school));

        School actualSchool = schoolService.get(TEST_SCHOOL_ID);

        inOrder.verify(schoolRepository, times(1)).findById(TEST_SCHOOL_UUID);
        assertEquals(school, actualSchool);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("Failure during getting school without ID")
    void getFailure_NoId() {
        assertThrows(MissingMandatoryFieldException.class, () -> schoolService.get(""));
    }

    @Test
    @DisplayName("Success during deleting school")
    void deleteSuccess() {
        School school = createSchool();
        school.setId(TEST_SCHOOL_UUID);
        when(schoolRepository.findById(TEST_SCHOOL_UUID)).thenReturn(Optional.of(school));

        String result = schoolService.delete(TEST_SCHOOL_ID);

        inOrder.verify(schoolRepository, times(1)).findById(TEST_SCHOOL_UUID);
        assertEquals("Delete success", result);
        inOrder.verify(schoolRepository).deleteById(TEST_SCHOOL_UUID);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("Failure during deleting school when not found in the database")
    void deleteFailure_NotFoundInDatabase() {
        when(schoolRepository.findById(TEST_SCHOOL_UUID)).thenReturn(Optional.empty());
        assertThrows(SchoolNotFoundException.class, () -> schoolService.delete(TEST_SCHOOL_ID));
    }

    @Test
    @DisplayName("Success during updating school")
    void updateSuccess() {
        SchoolDTO schoolDTO = createSchoolDTO();
        School school = createSchool();
        when(schoolRepository.findById(TEST_SCHOOL_UUID)).thenReturn(Optional.of(school));

        String actualResult = schoolService.update(schoolDTO, TEST_SCHOOL_ID);

        inOrder.verify(schoolRepository, times(1)).findById(TEST_SCHOOL_UUID);
        inOrder.verify(schoolRepository, times(1)).save(school);
        assertEquals("Update success", actualResult);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("Failure during updating school without ID")
    void updateFailure_NoId() {
        SchoolDTO schoolDTO = createSchoolDTO();
        assertThrows(MissingMandatoryFieldException.class, () -> schoolService.update(schoolDTO, ""));
    }


    private SchoolDTO createSchoolDTO() {
        SchoolDTO schoolDTO = new SchoolDTO();
        schoolDTO.setName(TEST_SCHOOL_NAME);
        schoolDTO.setAddress(TEST_SCHOOL_ADDRESS);
        schoolDTO.setPhoneNumber(TEST_SCHOOL_PHONE_NUMBER);
        schoolDTO.setPostalCode(TEST_SCHOOL_POSTAL_CODE);

        return schoolDTO;
    }

    private School createSchool() {
        School inputSchool = new School();
        inputSchool.setName(TEST_SCHOOL_NAME);
        inputSchool.setAddress(TEST_SCHOOL_ADDRESS);
        inputSchool.setPhoneNumber(TEST_SCHOOL_PHONE_NUMBER);
        inputSchool.setPostalCode(TEST_SCHOOL_POSTAL_CODE);
        return inputSchool;
    }

}