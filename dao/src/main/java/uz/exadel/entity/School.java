package uz.exadel.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import uz.exadel.dtos.SchoolDTO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        School school = (School) o;
        return Objects.equals(id, school.id) && Objects.equals(name, school.name) && Objects.equals(address, school.address) && Objects.equals(phoneNumber, school.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, address, phoneNumber);
    }
}
