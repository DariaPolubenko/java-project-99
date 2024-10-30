package hexlet.code.dto.task;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class CreateTaskDTO {
    private JsonNullable<Integer> index;
    private JsonNullable<Long> assigneeId;

    @NotNull
    private String title;

    private JsonNullable<String> content;

    @NotNull
    private String status;
}

