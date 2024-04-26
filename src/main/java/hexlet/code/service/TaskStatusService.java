package hexlet.code.service;

import hexlet.code.dto.taskStatuses.TaskStatusCreateDTO;
import hexlet.code.dto.taskStatuses.TaskStatusDTO;
import hexlet.code.dto.taskStatuses.TaskStatusUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskStatusMapper;
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
public class TaskStatusService {
    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskStatusMapper taskStatusMapper;

    public List<TaskStatusDTO> getAll() {
        var taskStatuses = taskStatusRepository.findAll();
        var result = taskStatuses.stream()
                .map(taskStatusMapper::map)
                .toList();
        return result;
    }

    public TaskStatusDTO create(TaskStatusCreateDTO taskStatusData) {
        try {
            TaskStatus taskStatus = taskStatusMapper.map(taskStatusData);
            taskStatusRepository.save(taskStatus);
            var taskStatusDTO = taskStatusMapper.map(taskStatus);
            return taskStatusDTO;
        } catch (NoSuchElementException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    public TaskStatusDTO findById(Long id) {
        var taskStatus = taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TaskStatus with id " + id + " not found!"));
        var taskStatusDTO = taskStatusMapper.map(taskStatus);
        return taskStatusDTO;
    }

    public TaskStatusDTO update(TaskStatusUpdateDTO taskStatusData, Long id) {
        try {
            var taskStatus = taskStatusRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("TaskStatus with id " + id + " not found!"));
            taskStatusMapper.update(taskStatusData, taskStatus);
            taskStatusRepository.save(taskStatus);
            var taskStatusDTO = taskStatusMapper.map(taskStatus);
            return taskStatusDTO;
        } catch (NoSuchElementException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());

        }
    }

    public void delete(Long id) {
        taskStatusRepository.deleteById(id);

//        TaskStatus taskStatus = taskStatusRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("TaskStatus with id " + id + " not found!"));
//        if (taskStatus != null) {
//            List<Task> tasks = taskRepository.findByTaskStatus(taskStatus);
//            if (tasks == null) {
//                taskStatusRepository.deleteById(id);
//            } else {
//                throw new ResponseStatusException(HttpStatus.CONFLICT, "It is not possible to remove the "
//                        + "user from the id " + id + " , as there are tasks attached to it");
//            }
    }
}
