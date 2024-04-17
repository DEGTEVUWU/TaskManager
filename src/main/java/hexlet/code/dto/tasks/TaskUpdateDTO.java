package hexlet.code.dto.tasks;

import com.fasterxml.jackson.annotation.JsonProperty;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.time.LocalDate;

@Getter
@Setter
public class TaskUpdateDTO {
//    @NotNull
    @JsonProperty("assignee_id")
    private Long assigneeId;

//    @NotBlank
    @JsonProperty("title")
    private String name;

//    @NotBlank
    @JsonProperty("content")
    private String description;

//    @NotBlank
    @JsonProperty("status")
    private String status;

}
