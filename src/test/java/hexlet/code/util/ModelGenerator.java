package hexlet.code.util;

import hexlet.code.dto.task.CreateTaskDTO;
import hexlet.code.dto.task.UpdateTaskDTO;
import hexlet.code.dto.user.CreateUserDTO;
import hexlet.code.dto.user.UpdateUserDTO;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
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
public class ModelGenerator {
    Faker faker = new Faker();

    private Model<User> user;
    private Model<CreateUserDTO> createUserDTO;
    private Model<UpdateUserDTO> updateUserDTO;

    private Model<TaskStatus> taskStatus;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;


    @PostConstruct
    private void init() {
        createUserModel();
        createTaskStatusModel();
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
}
