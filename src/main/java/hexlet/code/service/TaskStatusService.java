package hexlet.code.service;

import hexlet.code.dto.taskStatus.TaskStatusCreateDTO;
import hexlet.code.dto.taskStatus.TaskStatusDTO;
import hexlet.code.dto.taskStatus.TaskStatusUpdateDTO;
import hexlet.code.dto.users.UserCreateDTO;
import hexlet.code.dto.users.UserDTO;
import hexlet.code.dto.users.UserUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskStatusMapper;
import hexlet.code.mapper.UserMapper;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TaskStatusService {
    @Autowired
    private TaskStatusRepository repository;

    @Autowired
    private TaskStatusMapper taskStatusMapper;

//    @Autowired
//    private PasswordEncoder passwordEncoder;

    public List<TaskStatusDTO> getAll() {
        var taskStatuses = repository.findAll();
        var result = taskStatuses.stream()
                .map(taskStatusMapper::map)
                .toList();
        return result;
    }

    public TaskStatusDTO create(TaskStatusCreateDTO taskStatusData) {
        try {
            TaskStatus taskStatus = taskStatusMapper.map(taskStatusData);
//            task.set(passwordEncoder.encode(user.getPassword())); //хешируем пароль
            repository.save(taskStatus);
            var taskStatusDTO = taskStatusMapper.map(taskStatus);
            return taskStatusDTO;
        } catch(NoSuchElementException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    public TaskStatusDTO findById(Long id) {
        var taskStatus = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found!"));
        var taskStatusDTO = taskStatusMapper.map(taskStatus);
        return taskStatusDTO;
    }

    public TaskStatusDTO update(TaskStatusUpdateDTO taskStatusData, Long id) {
        try {
            var taskStatus = repository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found!"));
            taskStatusMapper.update(taskStatusData, taskStatus);
            repository.save(taskStatus);
            var taskStatusDTO = taskStatusMapper.map(taskStatus);
            return taskStatusDTO;
        } catch (NoSuchElementException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());

        }
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
