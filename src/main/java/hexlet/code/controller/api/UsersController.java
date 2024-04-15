package hexlet.code.controller.api;

import hexlet.code.dto.users.UserCreateDTO;
import hexlet.code.dto.users.UserDTO;
import hexlet.code.dto.users.UserUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.UserMapper;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.UserService;
import hexlet.code.utils.UserUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private UserService userService;

    @GetMapping(path = "")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<UserDTO>> index() {
//        User currentUser = userUtils.getCurrentUser();
//        if (currentUser == null)  throw  new ResourceNotFoundException("UNAUTHORIZED!");

        List<UserDTO> users = userService.getAll();
        return ResponseEntity.ok().body(users);
    }

    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserDTO> create(@Valid @RequestBody UserCreateDTO userData) throws NoSuchAlgorithmException, InvalidKeySpecException {
//        User user = userMapper.map(userData);
//        user.setPasswordDigest(passwordEncoder.encode(user.getPassword())); //хешировать пароль
//        userRepository.save(user);
//        UserDTO userDTO = userMapper.map(user);
        UserDTO user = userService.create(userData);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @GetMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserDTO> show(@PathVariable Long id) {
//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found."));
//        UserDTO userDTO = userMapper.map(user);
        UserDTO user = userService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PutMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserDTO> update(@RequestBody @Valid UserUpdateDTO userData, @PathVariable Long id) {
//        var user = userRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found."));
////        Long idCat = product.getCategory().getId();
////        Category category = categoryRepository.findById(idCat)
////                .orElseThrow(() -> new ResourceNotFoundException("Not Found: " + id));
//
//        userMapper.update(userData, user);
////        product.setCategory(category);
////        product.setCategory(category);
//        userRepository.save(user);
//        var userDTO = userMapper.map(user);
        UserDTO user = userService.update(userData, id);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}