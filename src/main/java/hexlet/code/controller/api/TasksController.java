package hexlet.code.controller.api;

import hexlet.code.dto.taskStatuses.TaskStatusCreateDTO;
import hexlet.code.dto.taskStatuses.TaskStatusDTO;
import hexlet.code.dto.taskStatuses.TaskStatusUpdateDTO;
import hexlet.code.dto.tasks.TaskCreateDTO;
import hexlet.code.dto.tasks.TaskDTO;
import hexlet.code.dto.tasks.TaskUpdateDTO;
import hexlet.code.service.TaskService;
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
@RequestMapping("/api/tasks")
public class TasksController {
    @Autowired
    private TaskService tasksService;

    @GetMapping(path = "")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<TaskDTO>> index() {
        List<TaskDTO> tasks = tasksService.getAll();
        return ResponseEntity.ok().body(tasks);
    }

    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TaskDTO> create(@Valid @RequestBody TaskCreateDTO taskData) throws NoSuchAlgorithmException, InvalidKeySpecException {
        TaskDTO tasks =  tasksService.create(taskData);
        return ResponseEntity.status(HttpStatus.CREATED).body(tasks);
    }

    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TaskDTO> show(@PathVariable Long id) {
        TaskDTO tasks = tasksService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }

    @PutMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TaskDTO> update(@RequestBody @Valid TaskUpdateDTO taskData, @PathVariable Long id) {
        TaskDTO task = tasksService.update(taskData, id);
        return ResponseEntity.status(HttpStatus.OK).body(task);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tasksService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
