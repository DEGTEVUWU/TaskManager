package hexlet.code.dto.taskStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TaskStatusCreateDTO {

    @NotBlank
    @Size(min = 1)
    private String name;

    @NotBlank
    @Size(min = 1)
    private String slug;
}
