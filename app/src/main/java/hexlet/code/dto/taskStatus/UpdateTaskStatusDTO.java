package hexlet.code.dto.taskStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTaskStatusDTO {
    @NotNull
    @Size(min = 1)
    private String name;
}
