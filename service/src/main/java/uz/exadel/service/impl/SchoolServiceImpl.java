package uz.exadel.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.exadel.dtos.ResponseItem;
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
@RequiredArgsConstructor
public class SchoolServiceImpl implements SchoolService {
    private final SchoolRepository schoolRepository;

    @Override
    public ResponseItem add(SchoolDTO schoolDTO) {
        //TODO ? mapper or method
        School school = new School();
        school.fromDTO(schoolDTO);
        schoolRepository.save(school);
        return new ResponseItem(null, "Save success", true);
    }

    @Override
    public ResponseItem get(String id) {
        School school = schoolRepository.findById(UUID.fromString(id)).orElse(null);
        if (school == null) {
            throw new SchoolNotFoundException();
        }

        return new ResponseItem(school);
    }

    @Override
    public ResponseItem delete(String id) {
        UUID uuid = UUID.fromString(id);
        School school = schoolRepository.findById(uuid).orElse(null);
        if (school == null) {
            throw new SchoolNotFoundException();
        }

        schoolRepository.deleteById(uuid);
        return new ResponseItem(null, "Delete success", true);
    }

    @Override
    public ResponseItem update(SchoolDTO schoolDTO, String id) {
        School school = schoolRepository.findById(UUID.fromString(id)).orElse(null);
        if (school == null) {
            throw new SchoolNotFoundException();
        }

        School updatedSchool = school.fromDTO(schoolDTO);
        schoolRepository.save(updatedSchool);

        return new ResponseItem(null, "Update success", true);
    }

    @Override
    public ResponseItem getAll() {
        List<School> schools = schoolRepository.findAll();
        return new ResponseItem(schools);
    }
}
