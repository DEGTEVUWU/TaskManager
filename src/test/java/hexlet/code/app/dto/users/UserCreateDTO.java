package hexlet.code.app.dto.users;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

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
