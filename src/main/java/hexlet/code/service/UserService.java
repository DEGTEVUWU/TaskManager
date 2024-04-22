package hexlet.code.service;

import hexlet.code.dto.users.UserCreateDTO;
import hexlet.code.dto.users.UserDTO;
import hexlet.code.dto.users.UserUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.UserMapper;
import hexlet.code.model.Task;
import hexlet.code.model.User;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<UserDTO> getAll() {
        var users = userRepository.findAll();
        var result = users.stream()
                .map(userMapper::map)
                .toList();
        return result;
    }

    public UserDTO create(UserCreateDTO userData) {
        try {
            User user = userMapper.map(userData);
            user.setPasswordDigest(passwordEncoder.encode(user.getPassword())); //хешируем пароль
            userRepository.save(user);
            var userDTO = userMapper.map(user);
            return userDTO;
        } catch (NoSuchElementException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    public UserDTO findById(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found!"));
        var userDTO = userMapper.map(user);
        return userDTO;
    }

    public UserDTO update(UserUpdateDTO userData, Long id) {
        try {
            var user = userRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found!"));
            userMapper.update(userData, user);
            userRepository.save(user);
            var userDTO = userMapper.map(user);
            return userDTO;
        } catch (NoSuchElementException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());

        }
    }

    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found!"));
        if (user != null) {
            List<Task> tasks = taskRepository.findByAssignee(user);
            if (tasks.isEmpty()) {
                taskRepository.deleteById(id);
            } else {
                 throw new ResponseStatusException(HttpStatus.CONFLICT, "It is not possible to remove the " +
                         "user from the id " + id + " , as there are tasks attached to it");
            }
        }
    }
}
