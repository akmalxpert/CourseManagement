package uz.exadel.mapper;

import uz.exadel.dtos.StudentDTO;
import uz.exadel.entity.Student;

public interface StudentMapper {
    Student studentFromStudentDTO(StudentDTO studentDTO);
    Student studentFromStudentDTOUpdate(StudentDTO studentDTO, Student student);
}
