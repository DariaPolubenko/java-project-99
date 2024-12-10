package hexlet.code.dto.task;

import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;
import java.util.Set;

@Getter
@Setter
public class UpdateTaskDTO {
    private JsonNullable<String> title;
    private JsonNullable<String> content;
    private String status;
    private JsonNullable<Set<Long>> taskLabelIds;
}
