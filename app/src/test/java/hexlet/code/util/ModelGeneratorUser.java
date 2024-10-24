package hexlet.code.util;

import hexlet.code.dto.user.CreateUserDTO;
import hexlet.code.dto.user.UpdateUserDTO;
import hexlet.code.model.User;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.Select;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.stereotype.Component;

@Getter
@Component
public class ModelGeneratorUser {
    private Model<User> userModel;
    private Model<CreateUserDTO> createUserDTOModel;
    private Model<UpdateUserDTO> updateUserDTOModel;

    @PostConstruct
    private void init() {
        Faker faker = new Faker();
        userModel = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .supply(Select.field(User::getFirstName), () -> faker.name().firstName())
                .supply(Select.field(User::getLastName), () -> faker.name().lastName())
                .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                .supply(Select.field(User::getPasswordDigest), () -> faker.internet().password())
                .ignore(Select.field(User::getCreatedAt))
                .ignore(Select.field(User::getUpdatedAt))
                .toModel();

        createUserDTOModel = Instancio.of(CreateUserDTO.class)
                .supply(Select.field(CreateUserDTO::getEmail), () -> faker.internet().emailAddress())
                .supply(Select.field(CreateUserDTO::getFirstName), () -> JsonNullable.of(faker.name().firstName()))
                .supply(Select.field(CreateUserDTO::getLastName), () -> JsonNullable.of(faker.name().lastName()))
                .supply(Select.field(CreateUserDTO::getPassword), () -> faker.internet().password())
                .toModel();

        updateUserDTOModel = Instancio.of(UpdateUserDTO.class)
                .supply(Select.field(UpdateUserDTO::getEmail), () -> JsonNullable.of(faker.internet().emailAddress()))
                .supply(Select.field(UpdateUserDTO::getPassword), () -> JsonNullable.of(faker.internet().password()))
                .toModel();
    }
}
