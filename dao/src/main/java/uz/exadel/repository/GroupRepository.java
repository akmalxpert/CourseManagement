package uz.exadel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.exadel.entity.Group;
import uz.exadel.enums.FacultyEnum;

import java.util.List;
import java.util.UUID;

public interface GroupRepository extends JpaRepository<Group, UUID> {
    List<Group> findBySchoolId(UUID schoolId);
    List<Group> findBySchoolIdAndFaculty(UUID schoolId, FacultyEnum faculty);
}
