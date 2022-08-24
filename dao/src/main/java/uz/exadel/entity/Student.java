package uz.exadel.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Student {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(nullable = false, length = 25)
    private String fullName;

    private UUID groupId;
    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinColumn(name = "groupId", updatable = false, insertable = false)
    private Group group;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Course> courses;

    @Column(nullable = false)
    private Integer level;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(id, student.id) && Objects.equals(fullName, student.fullName) && Objects.equals(groupId, student.groupId) && Objects.equals(level, student.level);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullName, groupId, level);
    }
}
