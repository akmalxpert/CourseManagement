package uz.exadel.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeacherDTO {
    private String fullName;
    private List<String> positions;
    private String email;
    private String officePhoneNumber;
    private List<String> courses;
    private String schoolId;
}