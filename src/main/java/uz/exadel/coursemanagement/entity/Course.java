package uz.exadel.coursemanagement.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Course {
    @Id
    @GeneratedValue()
    private UUID id;


    private String name;

    private String description;

}
