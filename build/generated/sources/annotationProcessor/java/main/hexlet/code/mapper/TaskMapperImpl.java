package hexlet.code.mapper;

import hexlet.code.dto.task.CreateTaskDTO;
import hexlet.code.dto.task.TaskDTO;
import hexlet.code.dto.task.UpdateTaskDTO;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-10T17:51:00+0300",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.10.2.jar, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class TaskMapperImpl extends TaskMapper {

    @Autowired
    private JsonNullableMapper jsonNullableMapper;
    @Autowired
    private ReferenceMapper referenceMapper;

    @Override
    public Task map(CreateTaskDTO dto) {
        if ( dto == null ) {
            return null;
        }

        Task task = new Task();

        if ( jsonNullableMapper.isPresent( dto.getAssigneeId() ) ) {
            task.setAssignee( referenceMapper.toEntity( jsonNullableMapper.unwrap( dto.getAssigneeId() ), User.class ) );
        }
        task.setName( dto.getTitle() );
        if ( jsonNullableMapper.isPresent( dto.getContent() ) ) {
            task.setDescription( jsonNullableMapper.unwrap( dto.getContent() ) );
        }
        task.setTaskStatus( toEntity( dto.getStatus() ) );
        if ( jsonNullableMapper.isPresent( dto.getTaskLabelIds() ) ) {
            task.setLabels( toEntityLabel( jsonNullableMapper.unwrap( dto.getTaskLabelIds() ) ) );
        }
        if ( jsonNullableMapper.isPresent( dto.getIndex() ) ) {
            task.setIndex( jsonNullableMapper.unwrap( dto.getIndex() ) );
        }

        return task;
    }

    @Override
    public TaskDTO map(Task model) {
        if ( model == null ) {
            return null;
        }

        TaskDTO taskDTO = new TaskDTO();

        taskDTO.setAssigneeId( modelAssigneeId( model ) );
        taskDTO.setTitle( model.getName() );
        taskDTO.setContent( model.getDescription() );
        taskDTO.setStatus( modelTaskStatusSlug( model ) );
        taskDTO.setTaskLabelIds( jsonNullableMapper.wrap( toEntitySetIds( model.getLabels() ) ) );
        taskDTO.setId( model.getId() );
        taskDTO.setIndex( model.getIndex() );
        taskDTO.setCreatedAt( model.getCreatedAt() );

        return taskDTO;
    }

    @Override
    public Task map(TaskDTO model) {
        if ( model == null ) {
            return null;
        }

        Task task = new Task();

        task.setAssignee( referenceMapper.toEntity( model.getAssigneeId(), User.class ) );
        task.setName( model.getTitle() );
        task.setDescription( model.getContent() );
        task.setTaskStatus( toEntity( model.getStatus() ) );
        if ( jsonNullableMapper.isPresent( model.getTaskLabelIds() ) ) {
            task.setLabels( toEntityLabel( jsonNullableMapper.unwrap( model.getTaskLabelIds() ) ) );
        }
        task.setId( model.getId() );
        task.setIndex( model.getIndex() );
        task.setCreatedAt( model.getCreatedAt() );

        return task;
    }

    @Override
    public void update(UpdateTaskDTO dto, Task model) {
        if ( dto == null ) {
            return;
        }

        if ( jsonNullableMapper.isPresent( dto.getTitle() ) ) {
            model.setName( jsonNullableMapper.unwrap( dto.getTitle() ) );
        }
        if ( jsonNullableMapper.isPresent( dto.getContent() ) ) {
            model.setDescription( jsonNullableMapper.unwrap( dto.getContent() ) );
        }
        if ( dto.getStatus() != null ) {
            model.setTaskStatus( toEntity( dto.getStatus() ) );
        }
        if ( model.getLabels() != null ) {
            if ( jsonNullableMapper.isPresent( dto.getTaskLabelIds() ) ) {
                model.getLabels().clear();
                model.getLabels().addAll( toEntityLabel( jsonNullableMapper.unwrap( dto.getTaskLabelIds() ) ) );
            }
        }
        else {
            if ( jsonNullableMapper.isPresent( dto.getTaskLabelIds() ) ) {
                model.setLabels( toEntityLabel( jsonNullableMapper.unwrap( dto.getTaskLabelIds() ) ) );
            }
        }
    }

    private Long modelAssigneeId(Task task) {
        if ( task == null ) {
            return null;
        }
        User assignee = task.getAssignee();
        if ( assignee == null ) {
            return null;
        }
        Long id = assignee.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    private String modelTaskStatusSlug(Task task) {
        if ( task == null ) {
            return null;
        }
        TaskStatus taskStatus = task.getTaskStatus();
        if ( taskStatus == null ) {
            return null;
        }
        String slug = taskStatus.getSlug();
        if ( slug == null ) {
            return null;
        }
        return slug;
    }
}
