package hexlet.code.util;

import hexlet.code.dto.user.CreateUserDTO;
import hexlet.code.dto.user.UpdateUserDTO;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.Select;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.stereotype.Component;

@Getter
@Component
@RequiredArgsConstructor
public class ModelGenerator {
    Faker faker = new Faker();
    private Model<User> user;
    private Model<CreateUserDTO> createUserDTO;
    private Model<UpdateUserDTO> updateUserDTO;
    private Model<TaskStatus> taskStatus;
    private Model<Label> label;
    private Model<Task> task;

    @NonNull
    private UserRepository userRepository;

    @NonNull
    private TaskStatusRepository taskStatusRepository;

    @PostConstruct
    private void init() {
        createUserModel();
        createTaskStatusModel();
        createLabelModel();
        createTaskModel();
    }

    public void createUserModel() {
        user = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .supply(Select.field(User::getFirstName), () -> faker.name().firstName())
                .supply(Select.field(User::getLastName), () -> faker.name().lastName())
                .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                .supply(Select.field(User::getPasswordDigest), () -> faker.internet().password())
                .ignore(Select.field(User::getCreatedAt))
                .ignore(Select.field(User::getUpdatedAt))
                .toModel();

        createUserDTO = Instancio.of(CreateUserDTO.class)
                .supply(Select.field(CreateUserDTO::getEmail), () -> faker.internet().emailAddress())
                .supply(Select.field(CreateUserDTO::getFirstName), () -> JsonNullable.of(faker.name().firstName()))
                .supply(Select.field(CreateUserDTO::getLastName), () -> JsonNullable.of(faker.name().lastName()))
                .supply(Select.field(CreateUserDTO::getPassword), () -> faker.internet().password())
                .toModel();

        updateUserDTO = Instancio.of(UpdateUserDTO.class)
                .supply(Select.field(UpdateUserDTO::getEmail), () -> JsonNullable.of(faker.internet().emailAddress()))
                .supply(Select.field(UpdateUserDTO::getPassword), () -> JsonNullable.of(faker.internet().password()))
                .supply(Select.field(UpdateUserDTO::getFirstName), () -> JsonNullable.of(faker.name().firstName()))
                .supply(Select.field(UpdateUserDTO::getLastName), () -> JsonNullable.of(faker.name().lastName()))
                .toModel();
    }

    public void createTaskStatusModel() {
        taskStatus = Instancio.of(TaskStatus.class)
                .ignore(Select.field(TaskStatus::getId))
                .supply(Select.field(TaskStatus::getName), () -> faker.lorem().word())
                .supply(Select.field(TaskStatus::getSlug), () -> faker.internet().slug())
                .ignore(Select.field(TaskStatus::getCreatedAt))
                .toModel();
    }

    public void createLabelModel() {
        label = Instancio.of(Label.class)
                .ignore(Select.field(Label::getId))
                .ignore(Select.field(Label::getCreatedAt))
                .supply(Select.field(Label::getName), () -> faker.lorem().characters(3, 100).toString())
                .toModel();
    }

    public void createTaskModel() {
        task = Instancio.of(Task.class)
                .ignore(Select.field(Task::getId))
                .ignore(Select.field(Task::getLabels))
                .ignore(Select.field(Task::getAssignee))
                .ignore(Select.field(Task::getTaskStatus))
                .ignore(Select.field(Task::getCreatedAt))
                .supply(Select.field(Task::getName), () -> faker.lorem().word())
                .supply(Select.field(Task::getIndex), () -> faker.number().numberBetween(1,1000))
                .supply(Select.field(Task::getDescription), () -> faker.lorem().word())
                .toModel();
    }
}
