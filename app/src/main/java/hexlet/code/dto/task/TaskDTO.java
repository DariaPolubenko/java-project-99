package hexlet.code.dto.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TaskDTO {
    private Long id;
    private int index;
    private Long assigneeId;
    private String title;
    private String content;
    private String status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate createdAt;
}