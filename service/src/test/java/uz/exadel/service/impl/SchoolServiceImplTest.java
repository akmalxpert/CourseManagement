package uz.exadel.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import uz.exadel.dtos.ResponseData;
import uz.exadel.dtos.SchoolDTO;
import uz.exadel.entity.School;
import uz.exadel.exception.SchoolNotFoundException;
import uz.exadel.mapper.SchoolMapper;
import uz.exadel.repository.SchoolRepository;

import java.util.Collections;
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
    @DisplayName("Success while creating school")
    void createSuccess() {
        //given
        SchoolDTO schoolDTO = createSchoolDTO();
        School inputSchool = createSchool();

        //when
        ResponseData actualResult = schoolService.add(schoolDTO);

        //then
        inOrder.verify(schoolRepository, times(1)).save(inputSchool);
        assertEquals("Save success", actualResult.getDetail());
        inOrder.verifyNoMoreInteractions();
    }

    @ParameterizedTest
    @ValueSource(strings = {"ThisIsStringLongerThanTwentyFiveCharacters"})
    @DisplayName("Failure while creating school with longer name than specified in the constraints")
    void createFailure_LongerNameThanSpecifiedInConstraints(String strings) {
        //given
        SchoolDTO schoolDTO = createSchoolDTO();
        schoolDTO.setName(strings);

        School inputSchool = createSchool();
        inputSchool.setName(strings);

        when(schoolRepository.save(inputSchool)).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> schoolService.add(schoolDTO));
    }

    @Test
    @DisplayName("Success while getting school by id")
    void getSuccess() {
        School school = createSchool();
        school.setId(TEST_SCHOOL_UUID);
        when(schoolRepository.findById(TEST_SCHOOL_UUID)).thenReturn(Optional.of(school));

        ResponseData actualResult = schoolService.get(TEST_SCHOOL_ID);

        inOrder.verify(schoolRepository, times(1)).findById(TEST_SCHOOL_UUID);
        assertEquals(school, actualResult.getData());
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("Failure while getting school when not found in the database")
    void getFailure_NotFoundInDatabase() {
        when(schoolRepository.findById(TEST_SCHOOL_UUID)).thenReturn(Optional.empty());
        assertThrows(SchoolNotFoundException.class, () -> schoolService.get(TEST_SCHOOL_ID));
    }

    @Test
    @DisplayName("Success while deleting school")
    void deleteSuccess() {
        School school = createSchool();
        school.setId(TEST_SCHOOL_UUID);
        when(schoolRepository.findById(TEST_SCHOOL_UUID)).thenReturn(Optional.of(school));

        ResponseData actualResult = schoolService.delete(TEST_SCHOOL_ID);

        inOrder.verify(schoolRepository, times(1)).findById(TEST_SCHOOL_UUID);
        assertEquals("Delete success", actualResult.getDetail());
        inOrder.verify(schoolRepository).deleteById(TEST_SCHOOL_UUID);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("Failure while deleting school when not found in the database")
    void deleteFailure_NotFoundInDatabase() {
        when(schoolRepository.findById(TEST_SCHOOL_UUID)).thenReturn(Optional.empty());
        assertThrows(SchoolNotFoundException.class, () -> schoolService.delete(TEST_SCHOOL_ID));
    }

    @Test
    @DisplayName("Success while updating school")
    void updateSuccess() {
        SchoolDTO schoolDTO = createSchoolDTO();
        School school = createSchool();
        when(schoolRepository.findById(TEST_SCHOOL_UUID)).thenReturn(Optional.of(school));

        ResponseData actualResult = schoolService.update(schoolDTO, TEST_SCHOOL_ID);

        inOrder.verify(schoolRepository, times(1)).findById(TEST_SCHOOL_UUID);
        inOrder.verify(schoolRepository, times(1)).save(school);
        assertEquals("Update success", actualResult.getDetail());
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("Failure while updating school when not found in the database")
    void updateFailure_NotFoundInDatabase() {
        SchoolDTO schoolDTO = createSchoolDTO();
        when(schoolRepository.findById(TEST_SCHOOL_UUID)).thenReturn(Optional.empty());
        assertThrows(SchoolNotFoundException.class, () -> schoolService.update(schoolDTO, TEST_SCHOOL_ID));
    }

    @Test
    @DisplayName("Success while getting all schools")
    void successGetAll() {
        School school = createSchool();
        when(schoolRepository.findAll()).thenReturn(Collections.singletonList(school));
        ResponseData actualResult = schoolService.getAll();
        assertEquals(Collections.singletonList(school), actualResult.getData());
    }

    private SchoolDTO createSchoolDTO() {
        return new SchoolDTO(
                TEST_SCHOOL_NAME,
                TEST_SCHOOL_ADDRESS,
                TEST_SCHOOL_PHONE_NUMBER,
                TEST_SCHOOL_POSTAL_CODE
        );
    }

    private School createSchool() {
        SchoolDTO schoolDTO = createSchoolDTO();
        return SchoolMapper.INSTANCE.schoolToSchoolDTO(schoolDTO);
    }

}