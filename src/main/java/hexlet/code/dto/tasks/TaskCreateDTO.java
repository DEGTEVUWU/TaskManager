package hexlet.code.dto.tasks;

import hexlet.code.model.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskCreateDTO {
    @NotNull
    private Long index;

    @NotBlank
    private Long assigneeId;

    private String title;
    private String content;
    private TaskStatus status;

}
