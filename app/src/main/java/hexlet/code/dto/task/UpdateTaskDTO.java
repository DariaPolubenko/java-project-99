package hexlet.code.dto.task;

import hexlet.code.model.Label;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class UpdateTaskDTO {
    private JsonNullable<String> title;
    private JsonNullable<String> content;
    private String status;
    private JsonNullable<Set<Long>> taskLabelIds;
}
