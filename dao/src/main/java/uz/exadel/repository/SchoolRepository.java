package uz.exadel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.exadel.entity.School;

import java.util.UUID;

public interface SchoolRepository extends JpaRepository<School, UUID> {

}