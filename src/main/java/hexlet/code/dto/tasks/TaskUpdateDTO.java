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
    private Long assigneeId;

//    @NotBlank
    private String title;

//    @NotBlank
    private String content;

//    @NotBlank
    private String status;

}
