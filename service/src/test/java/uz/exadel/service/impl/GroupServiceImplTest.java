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
import uz.exadel.dtos.GroupDTO;
import uz.exadel.dtos.ResponseData;
import uz.exadel.entity.Group;
import uz.exadel.entity.School;
import uz.exadel.exception.GroupNotFoundException;
import uz.exadel.exception.SchoolNotFoundException;
import uz.exadel.mapper.GroupMapper;
import uz.exadel.repository.GroupRepository;
import uz.exadel.repository.SchoolRepository;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GroupServiceImplTest {
    private static final String TEST_GROUP_ID = "e94d0a42-411a-408a-a4d0-1de0009299ae";
    private static final String TEST_GROUP_NAME = "test-group-name";
    private static final Integer TEST_GROUP_LEVEL = 4;
    private static final String TEST_GROUP_FACULTY = "BUSINESS_INFORMATION_SYSTEMS";
    private static final String TEST_GROUP_SCHOOL_ID = "fdd11fd8-d7a5-4b4c-857b-65a03235c99b";
    private static final UUID TEST_GROUP_UUID = UUID.fromString(TEST_GROUP_ID);
    private static final UUID TEST_GROUP_SCHOOL_UUID = UUID.fromString(TEST_GROUP_SCHOOL_ID);

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private SchoolRepository schoolRepository;

    private InOrder inOrder;

    @BeforeEach
    void setUp() {
        inOrder = Mockito.inOrder(groupRepository, schoolRepository);
    }

    @InjectMocks
    private GroupServiceImpl groupService;

    @Test
    @DisplayName("Success while creating group")
    void createSuccess() {
        //given
        GroupDTO groupDTO = createGroupDTO();
        Group group = createGroup();

        //when
        when(schoolRepository.findById(TEST_GROUP_SCHOOL_UUID)).thenReturn(Optional.of(new School()));
        ResponseData actualResult = groupService.add(groupDTO);

        //then
        inOrder.verify(schoolRepository, times(1)).findById(TEST_GROUP_SCHOOL_UUID);
        inOrder.verify(groupRepository, times(1)).save(group);
        assertEquals("Save success", actualResult.getDetail());
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("Failure when creating group - school ID not found")
    void createFailure_SchoolIdNotFound() {
        GroupDTO groupDTO = createGroupDTO();

        when(schoolRepository.findById(TEST_GROUP_SCHOOL_UUID)).thenReturn(Optional.empty());

        assertThrows(SchoolNotFoundException.class, () -> groupService.add(groupDTO));
    }

    @Test
    @DisplayName("Success while getting group by id")
    void getSuccess() {
        Group group = createGroup();
        group.setId(TEST_GROUP_UUID);
        when(groupRepository.findById(TEST_GROUP_UUID)).thenReturn(Optional.of(group));

        ResponseData actualResult = groupService.get(TEST_GROUP_ID);

        inOrder.verify(groupRepository, times(1)).findById(TEST_GROUP_UUID);
        assertEquals(group, actualResult.getData());
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("Failure while getting group when not found in the database")
    void getFailure_NotFoundInDatabase() {
        when(groupRepository.findById(TEST_GROUP_UUID)).thenReturn(Optional.empty());
        assertThrows(GroupNotFoundException.class, () -> groupService.get(TEST_GROUP_ID));
    }

    @Test
    @DisplayName("Success while deleting group")
    void deleteSuccess() {
        Group group = createGroup();
        group.setId(TEST_GROUP_UUID);
        when(groupRepository.findById(TEST_GROUP_UUID)).thenReturn(Optional.of(group));

        ResponseData actualResult = groupService.delete(TEST_GROUP_ID);

        inOrder.verify(groupRepository, times(1)).findById(TEST_GROUP_UUID);
        assertEquals("Delete success", actualResult.getDetail());
        inOrder.verify(groupRepository).deleteById(TEST_GROUP_UUID);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("Failure while deleting group when not found in the database")
    void deleteFailure_NotFoundInDatabase() {
        when(groupRepository.findById(TEST_GROUP_UUID)).thenReturn(Optional.empty());
        assertThrows(GroupNotFoundException.class, () -> groupService.delete(TEST_GROUP_ID));
    }

    @Test
    @DisplayName("Success while updating group")
    void updateSuccess() {
        GroupDTO groupDTO = createGroupDTO();
        Group group = createGroup();
        when(groupRepository.findById(TEST_GROUP_UUID)).thenReturn(Optional.of(group));

        ResponseData actualResult = groupService.update(groupDTO, TEST_GROUP_ID);

        inOrder.verify(groupRepository, times(1)).findById(TEST_GROUP_UUID);
        inOrder.verify(groupRepository, times(1)).save(group);
        assertEquals("Update success", actualResult.getDetail());
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("Failure while updating group when not found in the database")
    void updateFailure_NotFoundInDatabase() {
        GroupDTO groupDTO = createGroupDTO();
        when(groupRepository.findById(TEST_GROUP_UUID)).thenReturn(Optional.empty());
        assertThrows(GroupNotFoundException.class, () -> groupService.update(groupDTO, TEST_GROUP_ID));
    }

    @Test
    @DisplayName("Failure while updating group when school not found in the database")
    void updateFailure_SchoolNotFoundInDatabase() {
        GroupDTO groupDTO = createGroupDTO();

        when(groupRepository.findById(TEST_GROUP_UUID)).thenReturn(Optional.of(new Group()));
        when(schoolRepository.findById(TEST_GROUP_SCHOOL_UUID)).thenReturn(Optional.empty());
        assertThrows(SchoolNotFoundException.class, () -> groupService.update(groupDTO, TEST_GROUP_ID));
    }

    @Test
    @DisplayName("Success while getting schools by school ID")
    void successGetBySchoolId() {
        when(schoolRepository.findById(TEST_GROUP_SCHOOL_UUID)).thenReturn(Optional.of(new School()));
        when(groupRepository.findBySchoolId(TEST_GROUP_SCHOOL_UUID)).thenReturn(new ArrayList<>());
        assertDoesNotThrow(() -> groupService.getBySchoolIdAndFaculty(TEST_GROUP_SCHOOL_ID, null));
    }

    private GroupDTO createGroupDTO() {
        return new GroupDTO(
                TEST_GROUP_NAME,
                TEST_GROUP_LEVEL,
                TEST_GROUP_FACULTY,
                TEST_GROUP_SCHOOL_ID
        );
    }

    private Group createGroup() {
        GroupDTO groupDTO = createGroupDTO();
        return GroupMapper.INSTANCE.groupToGroupDTO(groupDTO);
    }

}
