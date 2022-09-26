package uz.exadel.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
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
    private Set<Course> courses;

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

    public Student(UUID id, String fullName, UUID groupId, Set<Course> courses, Integer level) {
        this.id = id;
        this.fullName = fullName;
        this.groupId = groupId;
        this.courses = courses;
        this.level = level;
    }
}
