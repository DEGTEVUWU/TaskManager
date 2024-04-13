package hexlet.code.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import jdk.jfr.SettingDefinition;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = true)
    private String firstName;

    @Column(nullable = true)
    private String lastName;
    // EMAIL
    @Column(unique = true)
    @Email
    private String email;

//    @Column(nullable = false)
    @Size(min = 3)
    private String password;

    @LastModifiedDate
    private LocalDate updatedAt;

    @CreatedDate
    private LocalDate createdAt;
}
