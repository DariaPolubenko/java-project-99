package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.label.CreateLabelDTO;
import hexlet.code.dto.label.UpdateLabelDTO;
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
public class LabelControllerTest {
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

    private User user;
    private TaskStatus taskStatus;
    private Task task;
    private Label label;

    //Faker faker = new Faker();

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

        task = new Task();
        task.setName("test name");
        task.setIndex(10);
        task.setDescription("test description");
        task.setTaskStatus(taskStatus);
        task.setAssignee(user);
        taskRepository.save(task);

        label = new Label();
        label.setName("test label");
        label.getTasks().add(task);
        labelRepository.save(label);
    }

    @Test
    public void testIndex() throws Exception {
        var result = mockMvc.perform(get("/api/labels").with(adminToken))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
        assertThat(body).contains(label.getName());
    }

    @Test
    public void testShow() throws Exception {
        var result = mockMvc.perform(get("/api/labels/" + label.getId()).with(adminToken))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        System.out.println(body);
        assertThatJson(body).and(
                v -> v.node("id").isEqualTo(label.getId()),
                v -> v.node("name").isEqualTo(label.getName()),
                v -> v.node("createdAt").isEqualTo(label.getCreatedAt().toString()));
    }

    @Test
    public void testCreate() throws Exception {
        var data = new CreateLabelDTO();
        data.setName("test create label");

        var request = MockMvcRequestBuilders.post("/api/labels")
                .with(adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var labelCreated = labelRepository.findByName(data.getName()).get();
        assertNotNull(labelCreated);
    }

    @Test
    public void testUpdate() throws Exception {
        var data = new UpdateLabelDTO();
        data.setName("test update label");

        var request = MockMvcRequestBuilders.put("/api/labels/" + label.getId())
                .with(adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var labelUpdated = labelRepository.findById(label.getId()).get();

        assertThat(labelUpdated.getName()).isEqualTo(data.getName());
        assertThat(labelUpdated.getId()).isEqualTo(label.getId());
        assertThat(labelUpdated.getCreatedAt()).isEqualTo(label.getCreatedAt());
    }

    @Test
    public void testDelete() throws Exception {

        mockMvc.perform(delete("/api/labels/" + label.getId()))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(delete("/api/labels/" + label.getId()).with(adminToken))
                .andExpect(status().isOk());

        var labelDeleted = labelRepository.findById(label.getId());
        assertThat(labelDeleted).isEmpty();
    }
}
