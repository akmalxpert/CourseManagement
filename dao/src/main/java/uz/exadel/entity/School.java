package uz.exadel.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import uz.exadel.dtos.SchoolDTO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Data
public class School {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @Column(nullable = false, length = 25)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false, length = 13)
    private String phoneNumber;

    @Column(length = 7)
    private String postalCode;

    public School fromDTO(SchoolDTO schoolDTO) {
        this.setAddress(schoolDTO.getAddress());
        this.setName(schoolDTO.getName());
        this.setPhoneNumber(schoolDTO.getPhoneNumber());
        this.setPostalCode(schoolDTO.getPostalCode());
        return this;
    }
}
