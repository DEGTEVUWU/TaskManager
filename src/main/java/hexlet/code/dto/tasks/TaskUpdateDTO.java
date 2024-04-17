package hexlet.code.dto.tasks;

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
    private JsonNullable<Long> assigneeId;

//    @NotBlank
    private JsonNullable<String> title;

//    @NotBlank
    private JsonNullable<String> content;

//    @NotBlank
    private JsonNullable<TaskStatus> status;

}
