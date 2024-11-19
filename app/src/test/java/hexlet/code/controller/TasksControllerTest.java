package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.task.CreateTaskDTO;
import hexlet.code.dto.task.UpdateTaskDTO;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.util.ModelGenerator;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TasksControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ObjectMapper om;

    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor adminToken;

    @Autowired
    private ModelGenerator modelGenerator;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private TaskRepository taskRepository;


    @Autowired
    private LabelRepository labelRepository;

    private Task testTask;

    private User user;

    private TaskStatus taskStatus;

    Faker faker = new Faker();

    @Autowired
    private TaskMapper taskMapper;

    @BeforeEach
    public void setUp() {
        adminToken = jwt().jwt(builder -> builder.subject("hexlet@example.com"));

        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .apply(springSecurity())
                .build();

        user = Instancio.of(modelGenerator.getUser()).create();
        userRepository.save(user);

        taskStatus = Instancio.of(modelGenerator.getTaskStatus()).create();
        taskStatusRepository.save(taskStatus);

        testTask = new Task();
        testTask.setName("test name");
        //testTask.setIndex(10);
        //testTask.setDescription("test description");
        testTask.setTaskStatus(taskStatus);
        testTask.setAssignee(user);
        taskRepository.save(testTask);
    }

    @Test
    public void testIndex() throws Exception {
        var result = mockMvc.perform(get("/api/tasks").with(adminToken))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
        assertThat(body).contains(testTask.getName());
    }

    @Test
    public void testShow() throws Exception {
        var result = mockMvc.perform(get("/api/tasks/" + testTask.getId()).with(adminToken))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        System.out.println(body);
        assertThatJson(body).and(
                v -> v.node("id").isEqualTo(testTask.getId()),
                //v -> v.node("index").isEqualTo(testTask.getName()),
                v -> v.node("assignee_id").isEqualTo(testTask.getAssignee().getId()),
                v -> v.node("title").isEqualTo(testTask.getName()),
                //v -> v.node("content").isEqualTo(testTask.getDescription()),
                v -> v.node("status").isEqualTo(testTask.getTaskStatus().getSlug()),
                v -> v.node("createdAt").isEqualTo(testTask.getCreatedAt().toString()));
    }

    @Test
    public void testCreate() throws Exception {
        var data = createTaskDTO();
        data.setTitle("Test title");
        var taskStatus1 = Instancio.of(modelGenerator.getTaskStatus()).create();
        taskStatusRepository.save(taskStatus1);
        data.setStatus(taskStatus1.getSlug());

        var label = new Label();
        label.setName("bug");
        labelRepository.save(label);
        var labelList = new ArrayList<Long>();
        labelList.add(label.getId());
        data.setLabelIds(JsonNullable.of(labelList));


        var request1 = MockMvcRequestBuilders.post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request1)
                .andExpect(status().isUnauthorized());

        var request2 = MockMvcRequestBuilders.post("/api/tasks")
                .with(adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

       var result = mockMvc.perform(request2)
                .andExpect(status().isCreated())
                .andReturn();

        var taskCreated = taskRepository.findByName(data.getTitle()).get();

        assertNotNull(taskCreated);
        assertThat(taskCreated.getIndex()).isEqualTo(data.getIndex().get());
        assertThat(taskCreated.getAssignee().getId()).isEqualTo(data.getAssigneeId().get());
        assertThat(taskCreated.getName()).isEqualTo(data.getTitle());
        assertThat(taskCreated.getDescription()).isEqualTo(data.getContent().get());
        assertThat(taskCreated.getTaskStatus().getSlug()).isEqualTo(data.getStatus());
        //assertThat(taskCreated.getLabels().get(1)).isEqualTo(data.getLabelIds());

        /*
        var body = result.getResponse().getContentAsString();
        System.out.println(body);
        assertThatJson(body).and(
                v -> v.node("id").isEqualTo(testTask.getId()),
                //v -> v.node("index").isEqualTo(testTask.getName()),
                v -> v.node("assignee_id").isEqualTo(testTask.getAssignee().getId()),
                v -> v.node("title").isEqualTo(testTask.getName()),
                //v -> v.node("content").isEqualTo(testTask.getDescription()),
                v -> v.node("status").isEqualTo(testTask.getTaskStatus().getSlug()),
                v -> v.node("createdAt").isEqualTo(testTask.getCreatedAt().toString()));
         */
    }

    @Test
    public void testUpdate() throws Exception {
        var data = updateTaskDTO();

        var request = MockMvcRequestBuilders.put("/api/tasks/" + testTask.getId())
                .with(adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var taskUpdated = taskRepository.findById(testTask.getId()).get();

        assertThat(taskUpdated.getName()).isEqualTo(data.getTitle().get());
        assertThat(taskUpdated.getDescription()).isEqualTo(data.getContent().get());
    }


    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete("/api/tasks/" + testTask.getId()).with(adminToken))
                .andExpect(status().isOk());

        var taskStatusDeleted = taskRepository.findById(testTask.getId());
        assertThat(taskStatusDeleted).isEmpty();
    }

    public CreateTaskDTO createTaskDTO() {
        var data =  new CreateTaskDTO();
        var number = faker.number().numberBetween(1, 1000);

        data.setIndex(JsonNullable.of(number));
        data.setAssigneeId(JsonNullable.of(user.getId()));
        data.setTitle(faker.lorem().word());
        data.setContent(JsonNullable.of(faker.lorem().word()));
        data.setStatus(taskStatus.getSlug());
        return data;
    }

    public UpdateTaskDTO updateTaskDTO() {
        var data = new UpdateTaskDTO();
        data.setTitle(JsonNullable.of(faker.lorem().word()));
        data.setContent(JsonNullable.of(faker.lorem().word()));
        return data;
    }
}
