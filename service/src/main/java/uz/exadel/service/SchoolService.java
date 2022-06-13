package uz.exadel.service;

import org.springframework.stereotype.Service;
import uz.exadel.dtos.ResponseData;
import uz.exadel.dtos.SchoolDTO;

@Service
public interface SchoolService {
    ResponseData add(SchoolDTO schoolDTO);

    ResponseData get(String id);

    ResponseData delete(String id);

    ResponseData update(SchoolDTO schoolDTO, String id);

    ResponseData getAll();
}
