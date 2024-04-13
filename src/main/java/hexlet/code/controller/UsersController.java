package hexlet.code.controller;

import hexlet.code.dto.users.UserCreateDTO;
import hexlet.code.dto.users.UserDTO;
import hexlet.code.dto.users.UserUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.UserMapper;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.utils.PasswordEncoder;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UsersController {
    private static final Logger log = LoggerFactory.getLogger(UsersController.class);
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

//    @Autowired
//    private PasswordEncoder passwordEncoder;

    @GetMapping(path = "")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDTO> index() {
        var users = userRepository.findAll();
        var result = users.stream()
                .map(userMapper::map)
                .toList();

        return result;
    }

    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO create(@Valid @RequestBody UserCreateDTO userData) throws NoSuchAlgorithmException, InvalidKeySpecException {
        User user = userMapper.map(userData);
        user.setPassword(PasswordEncoder.encodePassword(user.getPassword())); //хешировать пароль
        userRepository.save(user);
        UserDTO userDTO = userMapper.map(user);
        return userDTO;
    }

    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO show(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found."));
        UserDTO userDTO = userMapper.map(user);
        return userDTO;
    }

    @PutMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDTO update(@RequestBody @Valid UserUpdateDTO userData, @PathVariable Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found."));
//        Long idCat = product.getCategory().getId();
//        Category category = categoryRepository.findById(idCat)
//                .orElseThrow(() -> new ResourceNotFoundException("Not Found: " + id));

        userMapper.update(userData, user);
//        product.setCategory(category);
//        product.setCategory(category);
        userRepository.save(user);
        var userDTO = userMapper.map(user);
        return userDTO;
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        userRepository.deleteById(id);
    }
}