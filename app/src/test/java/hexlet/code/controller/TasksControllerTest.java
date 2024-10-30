package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.model.Task;
import hexlet.code.repository.TaskRepository;
import hexlet.code.util.ModelGeneratorTask;
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
public class TasksControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ObjectMapper om;

    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor adminToken;

    @Autowired
    private ModelGeneratorTask modelGeneratorTask;

    @Autowired
    private TaskRepository taskRepository;

    private Task testTask;

    @BeforeEach
    public void setUp() {
        adminToken = jwt().jwt(builder -> builder.subject("hexlet@example.com"));

        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .apply(springSecurity())
                .build();

        testTask = Instancio.of(modelGeneratorTask.getTaskModel())
                .create();
        taskRepository.save(testTask);
    }

    @Test
    public void testIndex() throws Exception {
        var result = mockMvc.perform(get("/api/task").with(adminToken))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
        assertThat(body).contains(testTask.getName());
    }

    @Test
    public void testShow() throws Exception {
        var result = mockMvc.perform(get("/api/task/" + testTask.getId()).with(adminToken))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        System.out.println(body);
        assertThatJson(body).and(
                v -> v.node("id").isEqualTo(testTask.getId()),
                v -> v.node("index").isEqualTo(testTask.getName()),
                v -> v.node("assigneeId").isEqualTo(testTask.getAssignee().getId()),
                v -> v.node("title").isEqualTo(testTask.getName()),
                v -> v.node("content").isEqualTo(testTask.getDescription()),
                v -> v.node("status").isEqualTo(testTask.getTaskStatus().getSlug()),
                v -> v.node("createdAt").isEqualTo(testTask.getCreatedAt().toString()));
    }

    @Test
    public void testCreate() throws Exception {
        var data = Instancio.of(modelGeneratorTask.getCreateTaskDTOModel())
                .create();

        var request1 = MockMvcRequestBuilders.post("/api/task")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request1)
                .andExpect(status().isUnauthorized());

        var request2 = MockMvcRequestBuilders.post("/api/task")
                .with(adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request2)
                .andExpect(status().isCreated());

        var taskCreated = taskRepository.findById(testTask.getId()).get();

        assertNotNull(taskCreated);
        assertThat(taskCreated.getIndex()).isEqualTo(data.getIndex());
        assertThat(taskCreated.getAssignee().getId()).isEqualTo(data.getAssigneeId());
        assertThat(taskCreated.getName()).isEqualTo(data.getTitle());
        assertThat(taskCreated.getDescription()).isEqualTo(data.getContent());
        assertThat(taskCreated.getTaskStatus().getSlug()).isEqualTo(data.getStatus());
    }

    @Test
    public void testUpdate() throws Exception {
        var data = Instancio.of(modelGeneratorTask.getUpdateTaskDTOModel())
                .create();

        var request = MockMvcRequestBuilders.put("/api/task/" + testTask.getId())
                .with(adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var taskUpdated = taskRepository.findById(testTask.getId()).get();

        assertThat(taskUpdated.getName()).isEqualTo(data.getTitle());
        assertThat(taskUpdated.getDescription()).isEqualTo(data.getContent());
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete("/api/task/" + testTask.getId()).with(adminToken))
                .andExpect(status().isOk());

        var taskStatusDeleted = taskRepository.findById(testTask.getId());
        assertThat(taskStatusDeleted).isEmpty();
    }
}
