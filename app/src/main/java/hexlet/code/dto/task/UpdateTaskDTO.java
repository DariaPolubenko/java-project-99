package hexlet.code.dto.task;

import hexlet.code.model.Label;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.List;

@Getter
@Setter
public class UpdateTaskDTO {
    private JsonNullable<String> title;
    private JsonNullable<String> content;
    private JsonNullable<List<Long>> labelIds;
}
