package hexlet.code.dto.tasks;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @NotNull
//    @JsonProperty("assignee_id")
    private Long assignee_id;

    @NotBlank
    private String title;
    @NotBlank
    private String content;

    private String status;

}
