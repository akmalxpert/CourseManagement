package uz.exadel.mapper;

import uz.exadel.dtos.TeacherDTO;
import uz.exadel.entity.Teacher;


public interface TeacherMapper {

    Teacher teacherFromTeacherDTO(TeacherDTO teacherDTO);
    Teacher teacherFromTeacherDTOUpdate(TeacherDTO teacherDTO, Teacher teacher);
}