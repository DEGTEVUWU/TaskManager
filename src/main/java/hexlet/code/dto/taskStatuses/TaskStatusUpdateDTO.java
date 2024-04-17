package hexlet.code.dto.taskStatuses;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskStatusUpdateDTO {

    @NotBlank
    @Size(min = 1)
    private String name;

    @NotBlank
    @Size(min = 1)
    private String slug;
}
