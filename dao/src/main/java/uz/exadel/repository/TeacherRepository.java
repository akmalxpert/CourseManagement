package uz.exadel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.exadel.entity.Teacher;

import java.util.UUID;

public interface TeacherRepository extends JpaRepository<Teacher, UUID> {

}