package hexlet.code.util;

import hexlet.code.dto.task.CreateTaskDTO;
import hexlet.code.dto.task.UpdateTaskDTO;
import hexlet.code.model.Task;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.Select;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Getter
@Component
public class ModelGeneratorTask {
    private Model<Task> taskModel;
    private Model<CreateTaskDTO> createTaskDTOModel;
    private Model<UpdateTaskDTO> updateTaskDTOModel;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    private void init() {

        var taskStatuses = taskStatusRepository.findAll();
        var user = userRepository.findById(1L).get();

        Faker faker = new Faker();
        taskModel = Instancio.of(Task.class)
                .ignore(Select.field(Task::getId))
                .supply(Select.field(Task::getName), () -> "Task 1")
                .supply(Select.field(Task::getIndex), faker::number)
                .supply(Select.field(Task::getDescription), () -> "Test task 1")
                .supply(Select.field(Task::getTaskStatus), () -> taskStatuses.get(1))
                .supply(Select.field(Task::getAssignee), () -> user)
                .ignore(Select.field(Task::getCreatedAt))
                .toModel();

        createTaskDTOModel =  Instancio.of(CreateTaskDTO.class)
                .supply(Select.field(CreateTaskDTO::getIndex), faker::number)
                .supply(Select.field(CreateTaskDTO::getAssigneeId), () -> JsonNullable.of(user.getId()))
                .supply(Select.field(CreateTaskDTO::getTitle), () ->  "Task 2")
                .supply(Select.field(CreateTaskDTO::getContent), () ->  JsonNullable.of("Test task 2"))
                .supply(Select.field(CreateTaskDTO::getStatus), () ->  taskStatuses.get(2).getSlug())
                .toModel();

        updateTaskDTOModel =  Instancio.of(UpdateTaskDTO.class)
                .supply(Select.field(UpdateTaskDTO::getTitle), () ->  JsonNullable.of("Task 3"))
                .supply(Select.field(UpdateTaskDTO::getContent), () ->  JsonNullable.of("Test task 3"))
                .toModel();
    }
}
