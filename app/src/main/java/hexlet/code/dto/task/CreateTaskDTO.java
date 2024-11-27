package hexlet.code.dto.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.Set;

@Getter
@Setter
public class CreateTaskDTO {
    private JsonNullable<Integer> index;

    @JsonProperty("assignee_id")
    private JsonNullable<Long> assigneeId;

    @NotNull
    private String title;

    private JsonNullable<String> content;

    @NotNull
    private String status;

    private JsonNullable<Set<Long>> taskLabelIds;
}

