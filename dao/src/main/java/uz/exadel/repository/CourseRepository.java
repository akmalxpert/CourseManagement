package uz.exadel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.exadel.entity.Course;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

public interface CourseRepository extends JpaRepository<Course, UUID> {
    Set<Course> findByIdIn(Collection<UUID> id);
}