package hexlet.code.mapper;

import hexlet.code.dto.taskStatus.CreateTaskStatusDTO;
import hexlet.code.dto.taskStatus.TaskStatusDTO;
import hexlet.code.dto.taskStatus.UpdateTaskStatusDTO;
import hexlet.code.model.TaskStatus;
import org.mapstruct.*;


@Mapper(
        //uses = { JsonNullableMapper.class },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TaskStatusMapper {

    public abstract TaskStatus map(CreateTaskStatusDTO dto);
    public abstract TaskStatusDTO map(TaskStatus model);
    public abstract void update(UpdateTaskStatusDTO dto, @MappingTarget TaskStatus model);
    public abstract TaskStatus map(TaskStatusDTO model);
}