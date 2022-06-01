package uz.exadel.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import uz.exadel.enums.FacultyEnum;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
public class Group {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    private String name;

    private Integer level;

    @Enumerated(EnumType.STRING)
    private FacultyEnum faculty;

    private UUID schoolId;
    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinColumn(name = "schoolId", updatable = false, insertable = false)
    private School school;
}
