package hexlet.code.mapper;

import hexlet.code.dto.task.CreateTaskDTO;
import hexlet.code.dto.task.TaskDTO;
import hexlet.code.dto.task.UpdateTaskDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Mapper(
        uses = { JsonNullableMapper.class, ReferenceMapper.class },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TaskMapper {
    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Mapping(target = "assignee", source = "assigneeId")
    @Mapping(target = "name", source = "title")
    @Mapping(target = "description", source = "content")
    @Mapping(target = "taskStatus", source = "status")
    @Mapping(target = "labels", source = "labelIds")
    public abstract Task map(CreateTaskDTO dto);

    @Mapping(source = "assignee.id", target = "assigneeId")
    @Mapping(target = "title", source = "name")
    @Mapping(target = "content", source = "description")
    @Mapping(target = "status", source = "taskStatus.slug")
    public abstract TaskDTO map(Task model);

    @Mapping(target = "assignee", source = "assigneeId")
    @Mapping(target = "name", source = "title")
    @Mapping(target = "description", source = "content")
    @Mapping(target = "taskStatus", source = "status")
    public abstract Task map(TaskDTO model);

    @Mapping(target = "name", source = "title")
    @Mapping(target = "description", source = "content")
    @Mapping(target = "labels", source = "labelIds")
    public abstract void update(UpdateTaskDTO dto, @MappingTarget Task model);

    public TaskStatus toEntity(String slug) {
        return taskStatusRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Status not found"));
    }

    public Set<Label> toEntity(Set<Long> labelIds) {
        return labelIds == null
                ? new LinkedHashSet<>()
                : labelIds.stream()
                .map(id -> labelRepository.findById(id).orElseThrow())
                .collect(Collectors.toSet());
    }
}