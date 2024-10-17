package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.util.ModelGeneratorUser;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;

@SpringBootTest
@AutoConfigureMockMvc
class UsersControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext wac;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelGeneratorUser modelGeneratorUser;

	@Autowired
	private ObjectMapper om;

	private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;

	private User testUser;

	@BeforeEach
	public void setUp() {
		token = jwt().jwt(builder -> builder.subject("hexlet@example.com"));

		mockMvc = MockMvcBuilders.webAppContextSetup(wac)
				.defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
				.apply(springSecurity())
				.build();

		testUser = Instancio.of(modelGeneratorUser.getUserModel())
				.create();
		userRepository.save(testUser);
	}

	@Test
	public void testIndex() throws Exception {
		var result = mockMvc.perform(get("/api/users").with(token))
				.andExpect(status().isOk())
				.andReturn();

		var body = result.getResponse().getContentAsString();
		assertThatJson(body).isArray();
		assertThat(body).contains(testUser.getFirstName());
		assertThat(body).doesNotContain(testUser.getPasswordDigest());
	}

	@Test
	public void testIndexWithOutAuthorization() throws Exception {
		var result = mockMvc.perform(get("/api/users"))
				.andExpect(status().isUnauthorized());
	}

	@Test
	public void testShow() throws Exception {
		var result = mockMvc.perform(get("/api/users/" + testUser.getId()).with(token))
				.andExpect(status().isOk())
				.andReturn();

		var body = result.getResponse().getContentAsString();
		assertThatJson(body).and(
				v -> v.node("id").isEqualTo(testUser.getId()),
				v -> v.node("email").isEqualTo(testUser.getEmail()),
				v -> v.node("firstName").isEqualTo(testUser.getFirstName()));
	}

	@Test
	public void testShowWithWrongId() throws Exception {
		mockMvc.perform(get("/api/users/" + 10000).with(token))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testCreate() throws Exception {
		var data = Instancio.of(modelGeneratorUser.getCreateUserDTOModel())
				.create();

		var request = MockMvcRequestBuilders.post("/api/users")
				.with(token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(om.writeValueAsString(data));

		mockMvc.perform(request)
				.andExpect(status().isCreated());

		var user = userRepository.findByEmail(data.getEmail()).get();

		assertNotNull(user);
		assertThat(user.getFirstName()).isEqualTo(data.getFirstName().get());
		assertThat(user.getLastName()).isEqualTo(data.getLastName().get());
		assertThat(user.getPasswordDigest()).isEqualTo(data.getPasswordDigest());
	}

	@Test
	public void testCreateWithWrongPassword() throws Exception {
		var data = Instancio.of(modelGeneratorUser.getCreateUserDTOModel())
				.create();
		data.setPasswordDigest("12");

		var request = MockMvcRequestBuilders.post("/api/users")
				.with(token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(om.writeValueAsString(data));

		mockMvc.perform(request)
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testUpdate() throws Exception {
		var data = Instancio.of(modelGeneratorUser.getUpdateUserDTOModel())
				.create();

		var request = MockMvcRequestBuilders.put("/api/users/" + testUser.getId())
				.with(token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(om.writeValueAsString(data));

		mockMvc.perform(request)
				.andExpect(status().isOk());

		var user = userRepository.findById(testUser.getId()).get();

		assertThat(user.getId()).isEqualTo(testUser.getId());
		assertThat(user.getFirstName()).isEqualTo(testUser.getFirstName());
		assertThat(user.getEmail()).isEqualTo(data.getEmail().get());
		assertThat(user.getPasswordDigest()).isEqualTo(data.getPasswordDigest().get());
	}

	@Test
	public void testUpdateWithWrongEmail() throws Exception {
		var data = Instancio.of(modelGeneratorUser.getUpdateUserDTOModel())
				.create();
		data.setEmail(JsonNullable.of("wrong_email"));

		var request = MockMvcRequestBuilders.put("/api/users" + testUser.getId())
				.with(token)
				.contentType(MediaType.APPLICATION_JSON)
				.content(om.writeValueAsString(data));

		mockMvc.perform(request)
				.andExpect(status().isNotFound());
	}

	@Test
	public void testDelete() throws Exception {
		mockMvc.perform(delete("/api/users/" + testUser.getId()).with(token))
				.andExpect(status().isOk());

		var user = userRepository.findById(testUser.getId());
		assertThat(user).isEmpty();
	}

	@Test
	public void testDeleteWithWrongId() throws Exception {
		mockMvc.perform(delete("/api/users/" + 10000).with(token))
				.andExpect(status().isNotFound());
	}
}
