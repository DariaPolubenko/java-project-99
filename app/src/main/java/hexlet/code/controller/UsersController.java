package hexlet.code.controller;

import hexlet.code.dto.CreateUserDTO;
import hexlet.code.dto.UpdateUserDTO;
import hexlet.code.dto.UserDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.UserMapper;
import hexlet.code.repository.UserRepository;
import hexlet.code.util.UserUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserUtils userUtils;

    private static final String AUTHORIZATION = "authentication.getName() == @userRepository.findById(#id).get().getEmail()";

    @GetMapping()
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
        //System.out.println("Пароль CreateUserDTO: " + data.getPassword());
        //System.out.println("Пароль User до сохранения: " + user.getPasswordDigest());

        userRepository.save(user);
        //System.out.println("Пароль User после сохранения: " + user.getPasswordDigest());
        var userDTO = userMapper.map(user);
        return userDTO;
    }

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
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) throws AccessDeniedException {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));

        userRepository.delete(user);
    }

    /*
    public void getPrivateAccess(User user) {
        var currentUser = userUtils.getCurrentUser().getEmail();
        var admin = "hexlet@example.com";

        if (!(currentUser.equals(admin) || currentUser.equals(user.getEmail()))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }
     */
}
