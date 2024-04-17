package hexlet.code.service;

import hexlet.code.dto.taskStatuses.TaskStatusCreateDTO;
import hexlet.code.dto.taskStatuses.TaskStatusDTO;
import hexlet.code.dto.taskStatuses.TaskStatusUpdateDTO;
import hexlet.code.dto.tasks.TaskCreateDTO;
import hexlet.code.dto.tasks.TaskDTO;
import hexlet.code.dto.tasks.TaskUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.mapper.TaskStatusMapper;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TaskService {
    @Autowired
    private TaskRepository repository;

    @Autowired
    private TaskMapper taskMapper;

//    @Autowired
//    private PasswordEncoder passwordEncoder;

    public List<TaskDTO> getAll() {
        List<Task> tasks = repository.findAll();
        var result = tasks.stream()
                .map(taskMapper::map)
                .toList();
        return result;
    }

    public TaskDTO create(TaskCreateDTO taskData) {
        try {
            Task tasks = taskMapper.map(taskData);
//            task.set(passwordEncoder.encode(user.getPassword())); //хешируем пароль
            repository.save(tasks);
            var taskDTO = taskMapper.map(tasks);
            return taskDTO;
        } catch(NoSuchElementException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    public TaskDTO findById(Long id) {
        var task = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found!"));
        var taskDTO = taskMapper.map(task);
        return taskDTO;
    }

    public TaskDTO update(TaskUpdateDTO taskData, Long id) {
        try {
            var task = repository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + " not found!"));
            taskMapper.update(taskData, task);
            repository.save(task);
            var taskDTO = taskMapper.map(task);
            return taskDTO;
        } catch (NoSuchElementException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());

        }
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}

