package hexlet.code.component;

import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.TaskRepository;
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
    private final TaskStatusRepository taskStatusRepository;

    @Autowired
    private final TaskRepository taskRepository;

    @Autowired
    private final CustomUserDetailsService userService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        createUser();

        var slugs = new String[]{"draft", "to_review",
                "to_be_fixed", "to_publish", "published"};
        createTaskStatus(slugs);

        createAdminsTask();
    }

    public void createUser() {
        if (userRepository.findByEmail("hexlet@example.com").isEmpty()) {
            var email = "hexlet@example.com";
            var userData = new User();
            userData.setEmail(email);
            userData.setPasswordDigest("qwerty");

            userService.createUser(userData);
        }
    }

    public void createTaskStatus(String[] slugs) {
        var faker = new Faker();
        for (var i = 0; i < slugs.length; i++) {
            var taskStatus = new TaskStatus();
            var taskName = faker.internet().slug();
            taskStatus.setSlug(slugs[i]);
            taskStatus.setName(taskName);
            taskStatusRepository.save(taskStatus);
        }
    }

    public void createAdminsTask() {
        var assignee = userRepository.findByEmail("hexlet@example.com").get();
        var taskStatus = taskStatusRepository.findBySlug("draft").get();

        var task = new Task();
        task.setName("Task name");
        task.setIndex(1);
        task.setDescription("Task description");
        task.setTaskStatus(taskStatus);
        task.setAssignee(assignee);

        taskRepository.save(task);
    }
}
