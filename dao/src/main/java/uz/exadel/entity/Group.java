package uz.exadel.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import uz.exadel.enums.FacultyEnum;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "groups")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return Objects.equals(id, group.id) && Objects.equals(name, group.name) && Objects.equals(level, group.level) && faculty == group.faculty && Objects.equals(schoolId, group.schoolId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, level, faculty, schoolId);
    }
}
