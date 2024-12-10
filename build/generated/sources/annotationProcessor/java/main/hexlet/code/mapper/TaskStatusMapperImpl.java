package hexlet.code.mapper;

import hexlet.code.dto.taskStatus.CreateTaskStatusDTO;
import hexlet.code.dto.taskStatus.TaskStatusDTO;
import hexlet.code.dto.taskStatus.UpdateTaskStatusDTO;
import hexlet.code.model.TaskStatus;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-10T16:04:01+0300",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.10.2.jar, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class TaskStatusMapperImpl extends TaskStatusMapper {

    @Override
    public TaskStatus map(CreateTaskStatusDTO dto) {
        if ( dto == null ) {
            return null;
        }

        TaskStatus taskStatus = new TaskStatus();

        taskStatus.setName( dto.getName() );
        taskStatus.setSlug( dto.getSlug() );

        return taskStatus;
    }

    @Override
    public TaskStatusDTO map(TaskStatus model) {
        if ( model == null ) {
            return null;
        }

        TaskStatusDTO taskStatusDTO = new TaskStatusDTO();

        taskStatusDTO.setId( model.getId() );
        taskStatusDTO.setName( model.getName() );
        taskStatusDTO.setSlug( model.getSlug() );
        taskStatusDTO.setCreatedAt( model.getCreatedAt() );

        return taskStatusDTO;
    }

    @Override
    public TaskStatus map(TaskStatusDTO model) {
        if ( model == null ) {
            return null;
        }

        TaskStatus taskStatus = new TaskStatus();

        taskStatus.setId( model.getId() );
        taskStatus.setCreatedAt( model.getCreatedAt() );
        taskStatus.setName( model.getName() );
        taskStatus.setSlug( model.getSlug() );

        return taskStatus;
    }

    @Override
    public void update(UpdateTaskStatusDTO dto, TaskStatus model) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getName() != null ) {
            model.setName( dto.getName() );
        }
    }
}
