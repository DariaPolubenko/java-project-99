package hexlet.code.controller;

import hexlet.code.dto.user.CreateUserDTO;
import hexlet.code.dto.user.UpdateUserDTO;
import hexlet.code.dto.user.UserDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.UserMapper;
import hexlet.code.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    private static final String AUTHORIZATION = "authentication.getName() == @userRepository.findById(#id).get().getEmail()";

    @GetMapping
    public ResponseEntity<List<UserDTO>> index() {
        var users = userRepository.findAll();
        var usersDTO = users.stream()
                .map(userMapper::map)
                .toList();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(users.size()))
                .body(usersDTO);
    }

    @GetMapping("/{id}")
    public UserDTO show(@PathVariable Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
        var userDTO = userMapper.map(user);
        return userDTO;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO create(@Valid @RequestBody CreateUserDTO data) {
        var user = userMapper.map(data);
        userRepository.save(user);
        var userDTO = userMapper.map(user);
        return userDTO;
    }

    //не очень нравится решение с AUTHORIZATION, так как повторно обновить емаил не дает,
    //нужно выйти и зайти заново, чтобы обновился токен, котрый генерируется на основе email
    //наверное, правильнее ипсользовать роли, но пока не разбиралась с тем, как их назначать
    @PreAuthorize(AUTHORIZATION)
    @PutMapping("/{id}")
    public UserDTO update(@PathVariable Long id, @Valid @RequestBody UpdateUserDTO data) throws AccessDeniedException {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
        userMapper.update(data, user);
        userRepository.save(user);
        var userDTO = userMapper.map(user);
        return userDTO;
    }

    @PreAuthorize(AUTHORIZATION)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) throws AccessDeniedException {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
        userRepository.delete(user);
    }
}
