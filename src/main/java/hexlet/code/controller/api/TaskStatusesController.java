package hexlet.code.controller.api;

import hexlet.code.dto.taskStatuses.TaskStatusCreateDTO;
import hexlet.code.dto.taskStatuses.TaskStatusDTO;
import hexlet.code.dto.taskStatuses.TaskStatusUpdateDTO;
import hexlet.code.service.TaskStatusService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

@RestController
@RequestMapping("/api/task_statuses")
public class TaskStatusesController {
    @Autowired
    private TaskStatusService taskStatusService;

    @GetMapping(path = "")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<TaskStatusDTO>> index() {

        List<TaskStatusDTO> taskStatuses = taskStatusService.getAll();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(taskStatuses.size()))
                .body(taskStatuses);
    }

    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TaskStatusDTO> create(@Valid @RequestBody TaskStatusCreateDTO taskStatusData) throws NoSuchAlgorithmException, InvalidKeySpecException {
        TaskStatusDTO taskStatus =  taskStatusService.create(taskStatusData);
        return ResponseEntity.status(HttpStatus.CREATED).body(taskStatus);
    }

    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TaskStatusDTO> show(@PathVariable Long id) {
        TaskStatusDTO taskStatus = taskStatusService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(taskStatus);
    }

    @PutMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TaskStatusDTO> update(@RequestBody @Valid TaskStatusUpdateDTO taskStatusData, @PathVariable Long id) {
        TaskStatusDTO taskStatus = taskStatusService.update(taskStatusData, id);
        return ResponseEntity.status(HttpStatus.OK).body(taskStatus);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        taskStatusService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
