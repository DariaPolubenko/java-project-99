package hexlet.code.component;

import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final CustomUserDetailsService userService;

    @Autowired
    private final TaskStatusRepository taskStatusRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (userRepository.findByEmail("hexlet@example.com").isEmpty()) {
            var email = "hexlet@example.com";
            var userData = new User();
            userData.setEmail(email);
            userData.setPasswordDigest("qwerty");

            userService.createUser(userData);
        }

        taskStatusRepository.save(createTaskStatus("draft"));
        taskStatusRepository.save(createTaskStatus("to_review"));
        taskStatusRepository.save(createTaskStatus("to_be_fixed"));
        taskStatusRepository.save(createTaskStatus("to_publish"));
        taskStatusRepository.save(createTaskStatus("published"));
    }

    public TaskStatus createTaskStatus(String taskStatusStr) {
        var taskStatus = new TaskStatus();
        var faker = new Faker();
        var slug = faker.internet().slug();
        taskStatus.setSlug(taskStatusStr);
        taskStatus.setName(slug);
        return taskStatus;
    }
}
