package hexlet.code.dto.task;

import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class UpdateTaskDTO {
    private JsonNullable<String> title;
    private JsonNullable<String> content;
}
