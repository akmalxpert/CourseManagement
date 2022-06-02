package uz.exadel.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.exadel.dtos.SchoolDTO;
import uz.exadel.entity.School;
import uz.exadel.repository.SchoolRepository;
import uz.exadel.service.SchoolService;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SchoolServiceImpl implements SchoolService {
    private SchoolRepository schoolRepository;

    @Override
    public String add(SchoolDTO schoolDTO) {
        return null;
    }

    @Override
    public School get(String id) {
        return null;
    }

    @Override
    public String delete(String id) {
        return null;
    }

    @Override
    public String update(SchoolDTO schoolDTO, String id) {
        return null;
    }

    @Override
    public List<School> getAll() {
        return schoolRepository.findAll();
    }
}
