package hexlet.code.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserDTO {
    private String email;
    private String firstName;
    private String lastName;
    private String password;

}
