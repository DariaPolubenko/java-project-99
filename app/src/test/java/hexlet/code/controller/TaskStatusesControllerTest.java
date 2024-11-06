package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.taskStatus.CreateTaskStatusDTO;
import hexlet.code.dto.taskStatus.UpdateTaskStatusDTO;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.util.ModelGenerator;
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
class TaskStatusesControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ObjectMapper om;

    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor adminToken;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private ModelGenerator modelGenerator;

    private TaskStatus taskStatus;

    @BeforeEach
    public void setUp() {
        adminToken = jwt().jwt(builder -> builder.subject("hexlet@example.com"));

        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .apply(springSecurity())
                .build();

        taskStatus = Instancio.of(modelGenerator.getTaskStatus()).create();
        taskStatusRepository.save(taskStatus);
    }

    @Test
    public void testIndex() throws Exception {
        var result = mockMvc.perform(get("/api/task_statuses").with(adminToken))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
        assertThat(body).contains(taskStatus.getName());
    }

    @Test
    public void testShow() throws Exception {
        var result = mockMvc.perform(get("/api/task_statuses/" + taskStatus.getId()).with(adminToken))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        System.out.println(body);
        assertThatJson(body).and(
                v -> v.node("id").isEqualTo(taskStatus.getId()),
                v -> v.node("name").isEqualTo(taskStatus.getName()),
                v -> v.node("slug").isEqualTo(taskStatus.getSlug()),
                v -> v.node("createdAt").isEqualTo(taskStatus.getCreatedAt().toString()));
    }

    @Test
    public void testCreate() throws Exception {
        var data = new CreateTaskStatusDTO();
        data.setName("Draft");
        data.setSlug("draft_1");

        var request1 = MockMvcRequestBuilders.post("/api/task_statuses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request1)
                .andExpect(status().isUnauthorized());

        var request2 = MockMvcRequestBuilders.post("/api/task_statuses")
                .with(adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request2)
                .andExpect(status().isCreated());

        var taskStatusCreated = taskStatusRepository.findBySlug(data.getSlug()).get();

        assertNotNull(taskStatusCreated);
        assertThat(taskStatusCreated.getName()).isEqualTo(data.getName());
        assertThat(taskStatusCreated.getSlug()).isEqualTo(data.getSlug());
    }

    @Test
    public void testUpdate() throws Exception {
        var data = new UpdateTaskStatusDTO();
        data.setName("published");

        var request = MockMvcRequestBuilders.put("/api/task_statuses/" + taskStatus.getId())
                .with(adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var taskStatusUpdated = taskStatusRepository.findById(taskStatus.getId()).get();

        assertThat(taskStatusUpdated.getId()).isEqualTo(taskStatus.getId());
        assertThat(taskStatusUpdated.getName()).isEqualTo(data.getName());
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete("/api/task_statuses/" + taskStatus.getId()).with(adminToken))
                .andExpect(status().isOk());

        var taskStatusDeleted = taskStatusRepository.findById(taskStatus.getId());
        assertThat(taskStatusDeleted).isEmpty();
    }
}

