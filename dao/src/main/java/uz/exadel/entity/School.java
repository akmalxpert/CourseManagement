package uz.exadel.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import uz.exadel.dtos.SchoolDTO;

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

    private String name;

    private String address;

    private String phoneNumber;

    private String postalCode;

    public School fromDTO(SchoolDTO schoolDTO) {
        this.setAddress(schoolDTO.getAddress());
        this.setName(schoolDTO.getName());
        this.setPhoneNumber(schoolDTO.getPhoneNumber());
        this.setPostalCode(schoolDTO.getPostalCode());
        return this;
    }
}
