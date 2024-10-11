package hexlet.code.controller;

import hexlet.code.controller.dto.UserDTO;
import hexlet.code.mapper.UserMapper;
import hexlet.code.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UsersController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @GetMapping()
    public List<UserDTO> index() {
        var users = userRepository.findAll();
        var usersDTO = users.stream().map(u -> userMapper.map(u)).toList();
        return usersDTO;

    }
}
