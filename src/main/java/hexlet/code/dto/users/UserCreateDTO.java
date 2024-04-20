package hexlet.code.dto.users;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class UserCreateDTO {
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Column(unique=true)
    @Email
    private String email;

    @Size(min = 3)
    private String password;

}
