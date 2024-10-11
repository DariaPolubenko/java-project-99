package hexlet.code.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserDTO {
    private String email;
    private String password;
}
