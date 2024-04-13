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
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
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

    @Column(nullable = false)
    @Size(min = 3)
    private String password;

//    @Column(name = "created_at", updatable = false)
    @CreatedDate
    private LocalDate createdAt;

//    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDate updatedAt;
}
