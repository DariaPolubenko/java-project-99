package hexlet.code.controller;

import hexlet.code.dto.user.CreateUserDTO;
import hexlet.code.dto.user.UpdateUserDTO;
import hexlet.code.dto.user.UserDTO;
import hexlet.code.service.UserService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class UserController {
     @NonNull
    private UserService userService;
    private static final String AUTHORIZATION = "authentication.getName() == @userRepository.findById(#id).get().getEmail()";

    @GetMapping
    public ResponseEntity<List<UserDTO>> index() {
        var users = userService.getAll();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(users.size()))
                .body(users);
    }

    @GetMapping("/{id}")
    public UserDTO show(@PathVariable Long id) {
        var user = userService.findById(id);
        return user;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO create(@Valid @RequestBody CreateUserDTO data) {
        var user = userService.create(data);
        return user;
    }

    //не очень нравится решение с AUTHORIZATION, так как повторно обновить емаил не дает,
    //нужно выйти и зайти заново, чтобы обновился токен, котрый генерируется на основе email
    //наверное, правильнее ипсользовать роли, но пока не разбиралась с тем, как их назначать
    @PreAuthorize(AUTHORIZATION)
    @PutMapping("/{id}")
    public UserDTO update(@PathVariable Long id, @Valid @RequestBody UpdateUserDTO data) throws AccessDeniedException {
        var user = userService.update(id, data);
        return user;
    }

    @PreAuthorize(AUTHORIZATION)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) throws AccessDeniedException {
        userService.delete(id);
    }
}
