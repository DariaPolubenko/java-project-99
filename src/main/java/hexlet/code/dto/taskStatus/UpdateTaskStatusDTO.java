package hexlet.code.dto.taskStatus;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTaskStatusDTO {
    @NotBlank
    @Size(min = 1)
    private String name;

    @NotBlank
    @Column(unique = true)
    private String slug;
}
