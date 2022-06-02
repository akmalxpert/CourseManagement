package uz.exadel.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import uz.exadel.dtos.SchoolDTO;
import uz.exadel.entity.School;
import uz.exadel.exception.MissingMandatoryFieldException;
import uz.exadel.exception.SchoolNotFoundException;
import uz.exadel.repository.SchoolRepository;
import uz.exadel.service.SchoolService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class SchoolServiceImpl implements SchoolService {
    private final SchoolRepository schoolRepository;

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
        if (StringUtils.hasText(id)) {
            throw new MissingMandatoryFieldException("Id should not be empty");
        }

        School school = schoolRepository.findById(UUID.fromString(id)).orElse(null);
        if (school == null) {
            throw new SchoolNotFoundException();
        }

        return school;
    }

    @Override
    public String delete(String id) {
        if (StringUtils.hasText(id)) {
            throw new MissingMandatoryFieldException("Id should not be empty");
        }
        UUID uuid = UUID.fromString(id);
        School school = schoolRepository.findById(uuid).orElse(null);
        if (school == null) {
            throw new SchoolNotFoundException();
        }

        schoolRepository.deleteById(uuid);
        return "Delete success";
    }

    @Override
    public String update(SchoolDTO schoolDTO, String id) {
        if (StringUtils.hasText(id)) {
            throw new MissingMandatoryFieldException("Id should not be empty");
        }

        School school = schoolRepository.findById(UUID.fromString(id)).orElse(null);
        if (school == null) {
            throw new SchoolNotFoundException();
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
