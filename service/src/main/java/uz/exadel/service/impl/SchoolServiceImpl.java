package uz.exadel.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import uz.exadel.dtos.SchoolDTO;
import uz.exadel.entity.School;
import uz.exadel.exception.BadRequestException;
import uz.exadel.repository.SchoolRepository;
import uz.exadel.service.SchoolService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class SchoolServiceImpl implements SchoolService {
    private SchoolRepository schoolRepository;

    @Override
    public String add(SchoolDTO schoolDTO) {
        //TODO ? mapper or method
        School school = new School();
        school.fromDTO(schoolDTO);
        schoolRepository.save(school);
        return "Save success";
    }

    @Override
    public School get(String id) {
        if (id == null || id.isEmpty()) {
            throw new BadRequestException("Id should not be empty");
        }

        School school = schoolRepository.findById(UUID.fromString(id)).orElse(null);
        if (school == null) {
            throw new BadRequestException("School not found");
        }

        return school;
    }

    @Override
    public String delete(String id) {
        if (id == null || id.isEmpty()) {
            throw new BadRequestException("Id should not be empty");
        }
        UUID uuid = UUID.fromString(id);
        School school = schoolRepository.findById(uuid).orElse(null);
        if (school == null) {
            throw new BadRequestException("School not found");
        }

        schoolRepository.deleteById(uuid);
        return "Delete success";
    }

    @Override
    public String update(SchoolDTO schoolDTO, String id) {
        if (id == null || id.isEmpty()) {
            throw new BadRequestException("Id should not be empty");
        }

        School school = schoolRepository.findById(UUID.fromString(id)).orElse(null);
        if (school == null) {
            throw new BadRequestException("School not found");
        }

        School updatedSchool = school.fromDTO(schoolDTO);
        schoolRepository.save(updatedSchool);

        return "Update success";
    }

    @Override
    public List<School> getAll() {
        return schoolRepository.findAll();
    }
}
