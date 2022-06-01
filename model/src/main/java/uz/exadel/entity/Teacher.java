package uz.exadel.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import uz.exadel.enums.TeacherPositionEnum;

import javax.persistence.*;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class Teacher {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    private String fullName;

    @ElementCollection(targetClass = TeacherPositionEnum.class)
    private EnumSet<TeacherPositionEnum> positions;

    private String email;

    private String officePhoneNumber;

    @ManyToMany
    private List<Course> courses;

    private UUID schoolId;
    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinColumn(name = "schoolId", updatable = false, insertable = false)
    private School school;
}
