package uz.exadel.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Data
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

}
