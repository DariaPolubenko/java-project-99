package hexlet.code.dto.task;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;
import java.util.Set;

@Getter
@Setter
public class UpdateTaskDTO {
    private JsonNullable<String> title;
    private String status;
    private JsonNullable<String> content;
    private JsonNullable<Set<Long>> taskLabelIds;

}
