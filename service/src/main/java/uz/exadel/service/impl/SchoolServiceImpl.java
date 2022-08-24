package uz.exadel.service.impl;

import org.springframework.stereotype.Service;
import uz.exadel.dtos.ResponseData;
import uz.exadel.dtos.SchoolDTO;
import uz.exadel.entity.School;
import uz.exadel.exception.SchoolNotFoundException;
import uz.exadel.repository.SchoolRepository;
import uz.exadel.service.SchoolService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class SchoolServiceImpl implements SchoolService {
    private final SchoolRepository schoolRepository;

    public SchoolServiceImpl(SchoolRepository schoolRepository) {
        this.schoolRepository = schoolRepository;
    }

    @Override
    public ResponseData add(SchoolDTO schoolDTO) {
        School school = new School();
        school.fromDTO(schoolDTO);
        schoolRepository.save(school);
        return new ResponseData(null, "Save success");
    }

    @Override
    public ResponseData get(String id) {
        School school = schoolRepository.findById(UUID.fromString(id)).orElse(null);
        if (school == null) {
            throw new SchoolNotFoundException();
        }

        return new ResponseData(school);
    }

    @Override
    public ResponseData delete(String id) {
        UUID uuid = UUID.fromString(id);
        School school = schoolRepository.findById(uuid).orElse(null);
        if (school == null) {
            throw new SchoolNotFoundException();
        }

        schoolRepository.deleteById(uuid);
        return new ResponseData(null, "Delete success");
    }

    @Override
    public ResponseData update(SchoolDTO schoolDTO, String id) {
        School school = schoolRepository.findById(UUID.fromString(id)).orElse(null);
        if (school == null) {
            throw new SchoolNotFoundException();
        }

        School updatedSchool = school.fromDTO(schoolDTO);
        schoolRepository.save(updatedSchool);

        return new ResponseData(null, "Update success");
    }

    @Override
    public ResponseData getAll() {
        List<School> schools = schoolRepository.findAll();
        return new ResponseData(schools);
    }
}
