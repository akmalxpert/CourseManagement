package uz.exadel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.exadel.entity.Teacher;

import java.util.List;
import java.util.UUID;

public interface TeacherRepository extends JpaRepository<Teacher, UUID> {
    List<Teacher> findAllBySchoolId(UUID schoolId);
}