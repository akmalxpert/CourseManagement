package uz.exadel.service;

import org.springframework.stereotype.Service;
import uz.exadel.dtos.SchoolDTO;
import uz.exadel.entity.School;

import java.util.List;

@Service
public interface SchoolService {
    String add(SchoolDTO schoolDTO);

    School get(String id);

    String delete(String id);

    String update(SchoolDTO schoolDTO, String id);

    List<School> getAll();
}
