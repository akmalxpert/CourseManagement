package uz.exadel.service;

import org.springframework.stereotype.Service;
import uz.exadel.dtos.ResponseItem;
import uz.exadel.dtos.SchoolDTO;
import uz.exadel.entity.School;

import java.util.List;

@Service
public interface SchoolService {
    ResponseItem add(SchoolDTO schoolDTO);

    ResponseItem get(String id);

    ResponseItem delete(String id);

    ResponseItem update(SchoolDTO schoolDTO, String id);

    ResponseItem getAll();
}
