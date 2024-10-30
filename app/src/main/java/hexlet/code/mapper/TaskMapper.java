package hexlet.code.mapper;

import hexlet.code.dto.task.CreateTaskDTO;
import hexlet.code.dto.task.TaskDTO;
import hexlet.code.dto.task.UpdateTaskDTO;
import hexlet.code.model.Task;
import org.mapstruct.*;


@Mapper(
        uses = { JsonNullableMapper.class },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TaskMapper {

    @Mapping(target = "assignee", source = "assigneeId")
    public abstract Task map(CreateTaskDTO dto);

    @Mapping(source = "assignee.id", target = "assigneeId")
    @Mapping(target = "title", source = "name")
    @Mapping(target = "content", source = "description")
    @Mapping(target = "status", source = "taskStatus.slug")
    public abstract TaskDTO map(Task model);

    @Mapping(target = "assignee.id", source = "assigneeId")
    @Mapping(target = "name", source = "title")
    @Mapping(target = "description", source = "content")
    @Mapping(target = "taskStatus.slug", source = "status")
    public abstract Task map(TaskDTO model);

    @Mapping(target = "name", source = "title")
    @Mapping(target = "description", source = "content")
    public abstract void update(UpdateTaskDTO dto, @MappingTarget Task model);
}